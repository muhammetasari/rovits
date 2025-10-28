import { Module } from '@nestjs/common';
// import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule } from '@nestjs/config';
import { GooglePlacesService } from './services/google-places.service';
import { AppController } from './app.controller';
import { PlaceFinderController } from './place-finder.controller';
// import { PoiModule } from './poi/poi.module';

@Module({
  imports: [
      ConfigModule.forRoot({ isGlobal: true }),
    //TypeOrmModule.forRoot({
      type: 'postgres',
      host: process.env.DB_HOST || 'localhost',
      port: parseInt(process.env.DB_PORT || '5432'),
      username: process.env.DB_USERNAME || 'postgres',
      password: process.env.DB_PASSWORD || 'postgres',
      database: process.env.DB_NAME || 'istanbul_tour',
      entities: [__dirname + '/**/*.entity{.ts,.js}'],
synchronize: true,
  logging: true,
}),
*/
// PoiModule,
],
controllers: [AppController, PlaceFinderController],
  providers: [GooglePlacesService],
})
export class AppModule {}