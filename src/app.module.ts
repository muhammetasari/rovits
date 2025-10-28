import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { GooglePlacesService } from './services/google-places.service';
import { AppController } from './app.controller';
import { PlaceFinderController } from './place-finder.controller';


@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
  ],
  controllers: [AppController, PlaceFinderController],
  providers: [GooglePlacesService],
})
export class AppModule {}