import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Poi, PoiCategory, DayOfWeek } from './poi.entity';
import { SpecialDay } from './special-day.entity';

@Injectable()
export class PoiService {
    constructor(
        @InjectRepository(Poi)
        private poiRepository: Repository<Poi>,

        @InjectRepository(SpecialDay)
        private specialDayRepository: Repository<SpecialDay>,
    ) {}

    async findAll(): Promise<Poi[]> {
        return this.poiRepository.find({
            relations: ['weeklySchedule'],
            order: { score: 'DESC' }
        });
    }

    async findById(id: number): Promise<Poi> {
        const poi = await this.poiRepository.findOne({
            where: { id },
            relations: ['weeklySchedule']
        });

        if (!poi) {
            throw new NotFoundException(`POI with ID ${id} not found`);
        }

        return poi;
    }

    async getTopByScore(limit: number): Promise<Poi[]> {
        return this.poiRepository.find({
            relations: ['weeklySchedule'],
            order: { score: 'DESC' },
            take: limit
        });
    }

    async getSpecialDay(date: Date): Promise<SpecialDay | undefined> {
        const dateStr = date.toISOString().split('T')[0];
        return this.specialDayRepository.findOne({
            where: { date: dateStr }
        });
    }

    async isAffectedBySpecialDay(poi: Poi, date: Date): Promise<boolean> {
        const specialDay = await this.getSpecialDay(date);
        if (!specialDay) return false;

        if (specialDay.isClosed) return true;

        if (specialDay.affectedCategories) {
            return specialDay.affectedCategories.includes(poi.category);
        }

        return false;
    }

    isPoiOpenAt(poi: Poi, date: Date, time: string): boolean {
        const dayName = date.toLocaleDateString('en-US', { weekday: 'long' }) as DayOfWeek;
        const schedules = poi.weeklySchedule.filter(s => s.day === dayName);

        if (schedules.length === 0 || schedules.every(s => s.isClosed)) {
            return false;
        }

        const [hour, minute] = time.split(':').map(Number);
        const timeInMinutes = hour * 60 + minute;

        return schedules.some(schedule => {
            if (schedule.isClosed) return false;

            const [openHour, openMin] = schedule.open.split(':').map(Number);
            const [closeHour, closeMin] = schedule.close.split(':').map(Number);

            const openTime = openHour * 60 + openMin;
            const closeTime = closeHour * 60 + closeMin;

            return timeInMinutes >= openTime && timeInMinutes <= closeTime;
        });
    }
}