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

  async findAll(locale: string = 'tr'): Promise<any[]> {
    const pois = await this.poiRepository.find({
      relations: ['translations', 'weeklySchedule'],
      order: { score: 'DESC' }
    });

    return pois.map(poi => this.formatPoi(poi, locale));
  }

  async findById(id: number, locale: string = 'tr'): Promise<any> {
    const poi = await this.poiRepository.findOne({
      where: { id },
      relations: ['translations', 'weeklySchedule']
    });
    
    if (!poi) {
      throw new NotFoundException(`POI with ID ${id} not found`);
    }
    
    return this.formatPoi(poi, locale);
  }

  async getTopByScore(limit: number, locale: string = 'tr'): Promise<any[]> {
    const pois = await this.poiRepository.find({
      relations: ['translations', 'weeklySchedule'],
      order: { score: 'DESC' },
      take: limit
    });

    return pois.map(poi => this.formatPoi(poi, locale));
  }

  async getSpecialDay(date: Date, locale: string = 'tr'): Promise<any | undefined> {
    const dateStr = date.toISOString().split('T')[0];
    const specialDay = await this.specialDayRepository.findOne({
      where: { date: dateStr },
      relations: ['translations']
    });

    if (!specialDay) return undefined;

    return {
      date: specialDay.date,
      name: specialDay.getName(locale),
      note: specialDay.getNote(locale),
      affectedCategories: specialDay.affectedCategories,
      isClosed: specialDay.isClosed
    };
  }

  async isAffectedBySpecialDay(poi: Poi, date: Date): Promise<boolean> {
    const dateStr = date.toISOString().split('T')[0];
    const specialDay = await this.specialDayRepository.findOne({
      where: { date: dateStr }
    });

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

  private formatPoi(poi: Poi, locale: string): any {
    return {
      id: poi.id,
      name: poi.getName(locale),
      description: poi.getDescription(locale),
      category: poi.category,
      latitude: poi.latitude,
      longitude: poi.longitude,
      score: poi.score,
      avgDuration: poi.avgDuration,
      queueTime: poi.queueTime,
      weeklySchedule: poi.weeklySchedule
    };
  }
}
