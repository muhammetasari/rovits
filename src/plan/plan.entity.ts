export class DailyPlan {
  day: number;
  date: string;
  totalDuration: number;
  totalDurationFormatted: string;
  slots: TimeSlot[];
  specialDay?: string;
}

export class TimeSlot {
  time: string;
  type: 'breakfast' | 'activity' | 'lunch' | 'dinner' | 'transfer';
  poi?: any;
  duration: number;
  travelTime?: number;
  description?: string;
  transport?: string;
}