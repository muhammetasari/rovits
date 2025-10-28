import { Entity, Column, PrimaryGeneratedColumn, OneToMany } from 'typeorm';

export enum PoiCategory {
    MUSEUM = 'museum',
    MOSQUE = 'mosque',
    LANDMARK = 'landmark',
    PALACE = 'palace',
    STREET = 'street',
    SQUARE = 'square',
    VIEWPOINT = 'viewpoint',
    MARKET = 'market',
    WATERFRONT = 'waterfront',
    RESTAURANT = 'restaurant'
}

export enum DayOfWeek {
    MONDAY = 'Monday',
    TUESDAY = 'Tuesday',
    WEDNESDAY = 'Wednesday',
    THURSDAY = 'Thursday',
    FRIDAY = 'Friday',
    SATURDAY = 'Saturday',
    SUNDAY = 'Sunday'
}

@Entity('poi')
export class Poi {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @Column({
        type: 'enum',
        enum: PoiCategory,
    })
    category: PoiCategory;

    @Column('decimal', { precision: 10, scale: 7 })
    latitude: number;

    @Column('decimal', { precision: 10, scale: 7 })
    longitude: number;

    @Column()
    score: number;

    @Column()
    avgDuration: number;

    @OneToMany(() => WeeklySchedule, schedule => schedule.poi, { eager: true, cascade: true })
    weeklySchedule: WeeklySchedule[];

    @Column({ nullable: true })
    queueTime: number; // Ortalama kuyruk süresi
}

@Entity('weekly_schedule')
export class WeeklySchedule {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({
        type: 'enum',
        enum: DayOfWeek,
    })
    day: DayOfWeek;

    @Column()
    open: string;

    @Column()
    close: string;

    @Column()
    isClosed: boolean;

    @Column()
    poiId: number;

    @Column(() => Poi, (poi: Poi) => poi.weeklySchedule)
    poi: Poi;
}