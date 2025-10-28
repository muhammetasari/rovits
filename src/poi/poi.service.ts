import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Poi, PoiCategory } from './poi.entity';

@Injectable()
export class PoiService {
  constructor(
    @InjectRepository(Poi)
    private poiRepository: Repository<Poi>,
  ) {}

  async findAll(): Promise<Poi[]> {
    return this.poiRepository.find({
      order: { score: 'DESC' }
    });
  }

  async findById(id: number): Promise<Poi> {
    const poi = await this.poiRepository.findOne({ where: { id } });
    if (!poi) {
      throw new NotFoundException(`POI with ID ${id} not found`);
    }
    return poi;
  }

  async getTopByScore(limit: number): Promise<Poi[]> {
    return this.poiRepository.find({
      order: { score: 'DESC' },
      take: limit
    });
  }
}