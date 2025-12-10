import type { Patient } from '../types/patient';
import type { PageResponse } from '../types/pageResponse';
import { apiClient } from './apiClient';

export const patientService = {
  async getAll(page: number = 0, size: number = 10): Promise<PageResponse<Patient>> {
    const response = await apiClient.get<PageResponse<Patient>>('/patient', {
      params: { page, size },
    });
    return response.data;
  },

  async getById(id: number): Promise<Patient> {
    const response = await apiClient.get<Patient>(`/patient/${id}`);
    return response.data;
  },

  async create(patient: Patient): Promise<Patient> {
    const response = await apiClient.post<Patient>('/patient', patient);
    return response.data;
  },

  async update(id: number, patient: Patient): Promise<Patient> {
    const response = await apiClient.put<Patient>(`/patient/${id}`, patient);
    return response.data;
  },

  async patch(id: number, patient: Partial<Patient>): Promise<Patient> {
    const response = await apiClient.patch<Patient>(`/patient/${id}`, patient);
    return response.data;
  },

  async delete(id: number): Promise<void> {
    await apiClient.delete(`/patient/${id}`);
  },
};
