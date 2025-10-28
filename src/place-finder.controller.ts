import { Controller, Get, Post, Query, Body } from '@nestjs/common';
import { GooglePlacesService } from './services/google-places.service';

@Controller('place-finder')
export class PlaceFinderController {
  constructor(private googlePlaces: GooglePlacesService) {}

  @Get('search')
  async search(@Query('q') query: string) {
    if (!query) {
      return { error: 'q parameter required' };
    }
    const result = await this.googlePlaces.searchPlace(query);
    return {
      query,
      placeId: result.places?.[0]?.id,
      name: result.places?.[0]?.displayName?.text,
      address: result.places?.[0]?.formattedAddress
    };
  }

  @Post('bulk-search')
  async bulkSearch(@Body() body: { queries: string[] }) {
    const results: any[] = []; // TYPE EKLE

    for (const query of body.queries) {
      try {
        const result = await this.googlePlaces.searchPlace(query);
        results.push({
          query,
          placeId: result.places?.[0]?.id,
          name: result.places?.[0]?.displayName?.text,
          address: result.places?.[0]?.formattedAddress
        });
        await new Promise(resolve => setTimeout(resolve, 200));
      } catch (error) {
        results.push({ query, error: 'Not found' });
      }
    }

    return results;
  }

  @Get('details')
  async getDetails(@Query('placeId') placeId: string) {
    if (!placeId) {
      return { error: 'placeId parameter required' };
    }
    return await this.googlePlaces.getPlaceDetails(placeId);
  }
}