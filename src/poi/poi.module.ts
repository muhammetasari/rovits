import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PoiController } from './poi.controller';
import { PoiService } from './poi.service';
import { Poi, PoiTranslation, WeeklySchedule } from './poi.entity';
import { SpecialDay, SpecialDayTranslation } from './special-day.entity';

@Module({
  imports: [
    TypeOrmModule.forFeature([
      Poi,
      PoiTranslation,
      WeeklySchedule,
      SpecialDay,
      SpecialDayTranslation
    ])
  ],
  controllers: [PoiController],
  providers: [PoiService],
  exports: [PoiService]
})
export class PoiModule {}
