import { Controller, Get, Param } from '@nestjs/common';
import { PoiService } from './poi.service';

@Controller('poi')
export class PoiController {
    constructor(private poiService: PoiService) {}

    @Get()
    getAllPois() {
        return this.poiService.findAll();
    }

    @Get('top/:limit')
    getTopPois(@Param('limit') limit: string) {
        return this.poiService.getTopByScore(Number(limit));
    }

    @Get(':id')
    getPoiById(@Param('id') id: string) {
        return this.poiService.findById(Number(id));
    }
}