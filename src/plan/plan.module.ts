import { Module } from '@nestjs/common';
import { PlanController } from './plan.controller';
import { PlanService } from './plan.service';
import { PoiModule } from '../poi/poi.module';

@Module({
    imports: [PoiModule],
    controllers: [PlanController],
    providers: [PlanService]
})
export class PlanModule {}