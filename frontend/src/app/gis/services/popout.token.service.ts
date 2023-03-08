import { InjectionToken } from '@angular/core';

export interface PopoutData {
  modalName: string;
  id: number;
  elementiWorkspace?: string;
  description?: string;
}

export const POPOUT_MODAL_DATA = new InjectionToken<PopoutData>('POPOUT_MODAL_DATA');

export enum PopoutModalName {
  'poligoniWorkspaceExpanded' = 'POLIGONI_WORKSPACE_EXPANDED',
}

export let POPOUT_MODALS = {
};

