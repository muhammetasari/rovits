export interface PoiCluster {
  id: number;
  center: { lat: number; lng: number };
  pois: any[];
  avgScore: number;
}