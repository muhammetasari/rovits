import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne } from 'typeorm';

@Entity('special_days')
export class SpecialDay {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ type: 'date' })
  date: string;

  @Column('text', { array: true, nullable: true })
  affectedCategories: string[];

  @Column({ default: false })
  isClosed: boolean;

  @OneToMany(() => SpecialDayTranslation, translation => translation.specialDay, { eager: true, cascade: true })
  translations: SpecialDayTranslation[];

  // Helper methods
  getName(locale: string = 'tr'): string {
    const translation = this.translations.find(t => t.locale === locale);
    return translation?.name || this.translations[0]?.name || 'Holiday';
  }

  getNote(locale: string = 'tr'): string {
    const translation = this.translations.find(t => t.locale === locale);
    return translation?.note || '';
  }
}

@Entity('special_day_translation')
export class SpecialDayTranslation {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  specialDayId: number;

  @Column({ length: 5 })
  locale: string;

  @Column()
  name: string;

  @Column('text', { nullable: true })
  note: string;

  @ManyToOne(() => SpecialDay, (specialDay: SpecialDay) => specialDay.translations, { onDelete: 'CASCADE' })
  specialDay: SpecialDay;
}
