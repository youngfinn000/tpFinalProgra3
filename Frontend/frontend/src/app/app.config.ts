import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import {
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import {
  provideLucideIcons,
  LucideIceCreamCone,
  LucideCakeSlice,
  LucideCupSoda,
  LucideFlame,
  LucidePackage,
  LucideEye,
} from '@lucide/angular';

import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideLucideIcons(
      LucideIceCreamCone,
      LucideCakeSlice,
      LucideCupSoda,
      LucideFlame,
      LucidePackage,
      LucideEye,
    ),
  ],
};