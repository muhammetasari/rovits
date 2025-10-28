import { Controller, Get, Query } from '@nestjs/common';
import { GooglePlacesService } from './services/google-places.service';

@Controller()
export class AppController {
  constructor(private googlePlaces: GooglePlacesService) {}

  @Get('test-place')
  async testPlace(@Query('placeId') placeId: string) {
    if (!placeId) {
      return { error: 'placeId parameter required' };
    }
    return await this.googlePlaces.getPlaceDetails(placeId);
  }

  @Get('search')  // 👈 BUNU EKLE
  async search(@Query('q') query: string) {
    if (!query) {
      return { error: 'q parameter required' };
    }
    return await this.googlePlaces.searchPlace(query);
  }
}