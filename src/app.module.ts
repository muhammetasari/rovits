import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule } from '@nestjs/config';
import { PoiModule } from './poi/poi.module';
import { PlanModule } from './plan/plan.module';
import { GooglePlacesService } from './services/google-places.service';



@Module({
    imports: [
        ConfigModule.forRoot({ isGlobal: true }),
        TypeOrmModule.forRoot({
            type: 'postgres',
            host: process.env.DB_HOST || 'localhost',
            port: parseInt(process.env.DB_PORT) || 5432,
            username: process.env.DB_USERNAME || 'postgres',
            password: process.env.DB_PASSWORD || 'postgres',
            database: process.env.DB_NAME || 'istanbul_tour',
            entities: [__dirname + '/**/*.entity{.ts,.js}'],
            synchronize: true,
            logging: true,
        }),
        PoiModule,
        PlanModule,
    ],
    providers: [GooglePlacesService],
})
export class AppModule {}