import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import {
  LucideAngularModule,
  IceCreamCone,
  CakeSlice,
  CupSoda,
  Flame,
  Package,
  Eye,
} from 'lucide-angular';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    importProvidersFrom(
      LucideAngularModule.pick({
        IceCreamCone,
        CakeSlice,
        CupSoda,
        Flame,
        Package,
        Eye,
      })
    ),
  ],
};