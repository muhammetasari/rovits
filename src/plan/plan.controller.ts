import { Controller, Get, Query } from '@nestjs/common';
import { PlanService } from './plan.service';

@Controller('plan')
export class PlanController {
  constructor(private planService: PlanService) {}

  @Get()
  generatePlan(
    @Query('days') days: string,
    @Query('transport') transport: string,
    @Query('locale') locale: string = 'tr'
  ) {
    const numDays = Number(days) || 3;
    const transportType = transport || 'public_transport';
    return this.planService.generatePlan(numDays, transportType, locale);
  }
}
