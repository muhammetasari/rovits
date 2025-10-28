import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';

@Entity('special_days')
export class SpecialDay {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    date: string;

    @Column()
    name: string;

    @Column('simple-array', { nullable: true })
    affectedCategories: string[];

    @Column()
    isClosed: boolean;

    @Column({ nullable: true })
    note: string;
}