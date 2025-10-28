import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PoiController } from './poi.controller';
import { PoiService } from './poi.service';
import { Poi } from './poi.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Poi])],
  controllers: [PoiController],
  providers: [PoiService],
  exports: [PoiService]
})
export class PoiModule {}