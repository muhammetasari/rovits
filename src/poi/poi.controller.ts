import { Controller, Get, Param, Query } from '@nestjs/common';
import { PoiService } from './poi.service';

@Controller('poi')
export class PoiController {
  constructor(private poiService: PoiService) {}

  @Get()
  getAllPois(@Query('locale') locale: string = 'tr') {
    return this.poiService.findAll(locale);
  }

  @Get('top/:limit')
  getTopPois(
    @Param('limit') limit: string,
    @Query('locale') locale: string = 'tr'
  ) {
    return this.poiService.getTopByScore(Number(limit), locale);
  }

  @Get(':id')
  getPoiById(
    @Param('id') id: string,
    @Query('locale') locale: string = 'tr'
  ) {
    return this.poiService.findById(Number(id), locale);
  }
}
