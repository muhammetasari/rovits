import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PoiController } from './poi.controller';
import { PoiService } from './poi.service';
import { Poi, WeeklySchedule } from './poi.entity';
import { SpecialDay } from './special-day.entity';

@Module({
    imports: [TypeOrmModule.forFeature([Poi, WeeklySchedule, SpecialDay])],
    controllers: [PoiController],
    providers: [PoiService],
    exports: [PoiService]
})
export class PoiModule {}