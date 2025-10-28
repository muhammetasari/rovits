import { Injectable } from '@nestjs/common';
import { PoiService } from '../poi/poi.service';
import { DailyPlan, TimeSlot } from './plan.entity';
import { Poi, DayOfWeek, PoiCategory } from '../poi/poi.entity';
import { PoiCluster } from './cluster.entity';
import { SpecialDay } from '../poi/special-day.entity';


@Injectable()
export class PlanService {
    constructor(private poiService: PoiService) {}

    async generatePlan(days: number, transportType: string = 'public_transport', locale: string = 'tr'): Promise<DailyPlan[]> {
        const plan: DailyPlan[] = [];
        const usedPoiIds = new Set<number>();

        const allAvailablePois = await this.poiService.findAll();
        const filteredPois = allAvailablePois.filter(p => p.category !== PoiCategory.RESTAURANT);

        const clusters = this.createClusters(filteredPois, days);

        for (let day = 1; day <= days; day++) {
            const date = new Date(Date.now() + day * 86400000);
            const cluster = clusters[day - 1];

            if (!cluster) continue;

            const availableForDay: any[] = [];
            for (const poi of cluster.pois) {
                const isAffected = await this.poiService.isAffectedBySpecialDay(poi, date);
                if (!usedPoiIds.has(poi.id) &&
                    this.isPoiOpen(poi, date) &&
                    !isAffected) {
                    availableForDay.push(poi);
                }
            }

            if (availableForDay.length === 0) continue;

            const sortedPois = this.optimizeRoute(availableForDay);
            const slots: TimeSlot[] = [];
            let currentTime = 9 * 60;

            sortedPois.forEach((poi, index) => {
                const nextPoi = sortedPois[index + 1];

                const queueTime = this.estimateQueueTime(poi);
                const totalPoiTime = poi.avgDuration + queueTime;

                const startHour = Math.floor(currentTime / 60);
                const startMin = currentTime % 60;
                const endTime = currentTime + totalPoiTime;
                const endHour = Math.floor(endTime / 60);
                const endMin = endTime % 60;

                const poiSlot: TimeSlot = {
                    time: `${startHour.toString().padStart(2, '0')}:${startMin.toString().padStart(2, '0')}-${endHour.toString().padStart(2, '0')}:${endMin.toString().padStart(2, '0')}`,
                    type: 'activity',
                    poi,
                    duration: totalPoiTime,
                    travelTime: 0
                };

                slots.push(poiSlot);
                usedPoiIds.add(poi.id);
                currentTime = endTime;

                if (nextPoi) {
                    const needsFerryTransfer = this.checkFerryTransfer(poi, nextPoi);

                    if (needsFerryTransfer) {
                        const ferryStart = currentTime;
                        const ferryEnd = currentTime + 30;

                        slots.push({
                            time: `${Math.floor(ferryStart / 60).toString().padStart(2, '0')}:${(ferryStart % 60).toString().padStart(2, '0')}-${Math.floor(ferryEnd / 60).toString().padStart(2, '0')}:${(ferryEnd % 60).toString().padStart(2, '0')}`,
                            type: 'transfer',
                            description: this.getTransferDescription(poi, nextPoi),
                            duration: 30,
                            transport: 'ferry'
                        });
                        currentTime = ferryEnd;
                    } else {
                        const travelTime = this.estimateTravelTime(poi, nextPoi, transportType);
                        poiSlot.travelTime = travelTime;
                        currentTime += travelTime;
                    }
                }
            });

            if (slots.length === 0) continue;

            const totalDuration = slots.reduce((sum, slot) =>
                sum + slot.duration + (slot.travelTime || 0), 0
            );

            const specialDay = await this.poiService.getSpecialDay(date);

            plan.push({
                day,
                date: date.toISOString().split('T')[0],
                totalDuration,
                totalDurationFormatted: this.formatDuration(totalDuration),
                slots,
                specialDay: specialDay?.name || undefined
            });
        }

        return plan;
    }

    private isPoiOpen(poi: any, date: Date): boolean {
        const dayName = date.toLocaleDateString('en-US', { weekday: 'long' }) as DayOfWeek;
        const schedules = poi.weeklySchedule.filter(s => s.day === dayName);

        return schedules.some(s => !s.isClosed);
    }

    private estimateQueueTime(poi: any): number {
        return poi.queueTime || 0;
    }

    private checkFerryTransfer(from: any, to: any): boolean {
        const fromSide = from.longitude < 29.0 ? 'europe' : 'asia';
        const toSide = to.longitude < 29.0 ? 'europe' : 'asia';

        return fromSide !== toSide;
    }

    private getTransferDescription(from: any, to: any): string {
        const fromSide = from.longitude < 29.0 ? 'Avrupa' : 'Asya';
        const toSide = to.longitude < 29.0 ? 'Avrupa' : 'Asya';

        return `${fromSide} Yakası → ${toSide} Yakası (Vapur)`;
    }

    private createClusters(pois: any[], clusterCount: number): PoiCluster[] {
        const effectiveClusters = Math.min(clusterCount, Math.floor(pois.length / 3));

        const centers = pois.slice(0, effectiveClusters).map(p => ({
            lat: p.latitude,
            lng: p.longitude
        }));

        const clusters: PoiCluster[] = centers.map((center, i) => ({
            id: i,
            center,
            pois: [],
            avgScore: 0
        }));

        pois.forEach(poi => {
            let minDist = Infinity;
            let nearestCluster = 0;

            clusters.forEach((cluster, index) => {
                const dist = this.distanceToPoint(poi, cluster.center);
                if (dist < minDist) {
                    minDist = dist;
                    nearestCluster = index;
                }
            });

            clusters[nearestCluster].pois.push(poi);
        });

        const allPois = [...pois];
        clusters.forEach(cluster => {
            while (cluster.pois.length < 3 && allPois.length > 0) {
                const availablePoi = allPois.find(p => !clusters.some(c => c.pois.includes(p)));
                if (availablePoi) {
                    cluster.pois.push(availablePoi);
                    allPois.splice(allPois.indexOf(availablePoi), 1);
                } else {
                    break;
                }
            }
            cluster.avgScore = cluster.pois.reduce((sum, p) => sum + p.score, 0) / cluster.pois.length;
        });

        return clusters.sort((a, b) => b.avgScore - a.avgScore);
    }

    private optimizeRoute(pois: any[]): any[] {
        if (pois.length === 0) return [];

        const sorted = [pois[0]];
        const remaining = pois.slice(1);

        while (remaining.length > 0) {
            const last = sorted[sorted.length - 1];
            let nearest = 0;
            let minDist = Infinity;

            remaining.forEach((poi, index) => {
                const dist = this.calculateDistance(last, poi);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = index;
                }
            });

            sorted.push(remaining[nearest]);
            remaining.splice(nearest, 1);
        }

        return sorted.slice(0, 3);
    }

    private distanceToPoint(poi: any, point: { lat: number; lng: number }): number {
        const R = 6371;
        const dLat = (point.lat - poi.latitude) * Math.PI / 180;
        const dLon = (point.lng - poi.longitude) * Math.PI / 180;
        const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(poi.latitude * Math.PI / 180) * Math.cos(point.lat * Math.PI / 180) *
            Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    private calculateDistance(poi1: any, poi2: any): number {
        const R = 6371;
        const dLat = (poi2.latitude - poi1.latitude) * Math.PI / 180;
        const dLon = (poi2.longitude - poi1.longitude) * Math.PI / 180;
        const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(poi1.latitude * Math.PI / 180) * Math.cos(poi2.latitude * Math.PI / 180) *
            Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    private estimateTravelTime(from: any, to: any, transportType: string): number {
        const distance = this.calculateDistance(from, to);

        let speed: number;
        switch(transportType) {
            case 'walking':
                speed = 5;
                break;
            case 'public_transport':
                speed = 20;
                break;
            case 'car':
                speed = 30;
                break;
            default:
                speed = 20;
        }

        return Math.round((distance / speed) * 60);
    }

    private formatDuration(minutes: number): string {
        const hours = Math.floor(minutes / 60);
        const mins = minutes % 60;
        return mins > 0 ? `${hours}h ${mins}m` : `${hours}h`;
    }
}