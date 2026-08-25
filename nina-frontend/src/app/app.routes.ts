import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Babybauch } from './pages/babybauch/babybauch';
import { Newborn } from './pages/newborn/newborn';
import { Familie } from './pages/familie/familie';
import { Kindergarten } from './pages/kindergarten/kindergarten';
import { Kontakt } from './pages/kontakt/kontakt';
import { Impressum } from './pages/impressum/impressum';
import { Datenschutz } from './pages/datenschutz/datenschutz';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'babybauch', component: Babybauch },
  { path: 'newborn', component: Newborn },
  { path: 'familie', component: Familie },
  { path: 'kindergarten', component: Kindergarten },
  { path: 'kontakt', component: Kontakt },
  { path: 'impressum', component: Impressum },
  { path: 'datenschutz', component: Datenschutz },
  { path: '**', redirectTo: '' }
];
