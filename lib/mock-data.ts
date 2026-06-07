import type { PlanNutricional, Nutricionista, ConsultaDietetica } from './types'

// Datos de ejemplo para desarrollo - en produccion estos vendran de tu API Django
export const planesEjemplo: PlanNutricional[] = [
  {
    id: 1,
    name: 'Plan Perdida de Peso',
    description: 'Plan personalizado para bajar de peso de forma saludable con deficit calorico controlado y macros balanceados.',
    goal: 'Perdida de peso',
    target_calories: 1500,
    duration_weeks: 12,
    estimated_cost: 149.99,
    is_active: true,
    created_at: '2024-01-15T10:00:00Z',
    updated_at: '2024-01-15T10:00:00Z'
  },
  {
    id: 2,
    name: 'Plan Masa Muscular',
    description: 'Dieta hipercalorica con alto contenido proteico para ganancia muscular optima.',
    goal: 'Ganancia muscular',
    target_calories: 2800,
    duration_weeks: 16,
    estimated_cost: 179.99,
    is_active: true,
    created_at: '2024-01-15T10:00:00Z',
    updated_at: '2024-01-15T10:00:00Z'
  },
  {
    id: 3,
    name: 'Plan Alimentacion Saludable',
    description: 'Plan equilibrado para mantener un estilo de vida saludable con todos los nutrientes necesarios.',
    goal: 'Mantenimiento',
    target_calories: 2000,
    duration_weeks: 8,
    estimated_cost: 99.99,
    is_active: true,
    created_at: '2024-01-15T10:00:00Z',
    updated_at: '2024-01-15T10:00:00Z'
  },
  {
    id: 4,
    name: 'Plan Keto',
    description: 'Dieta cetogenica baja en carbohidratos y alta en grasas saludables.',
    goal: 'Perdida de peso',
    target_calories: 1800,
    duration_weeks: 8,
    estimated_cost: 129.99,
    is_active: true,
    created_at: '2024-01-15T10:00:00Z',
    updated_at: '2024-01-15T10:00:00Z'
  },
  {
    id: 5,
    name: 'Plan Vegetariano',
    description: 'Plan nutricional completo sin carnes, rico en proteinas vegetales y nutrientes esenciales.',
    goal: 'Mantenimiento',
    target_calories: 1900,
    duration_weeks: 12,
    estimated_cost: 119.99,
    is_active: true,
    created_at: '2024-01-15T10:00:00Z',
    updated_at: '2024-01-15T10:00:00Z'
  },
  {
    id: 6,
    name: 'Plan Deportista',
    description: 'Nutricion optimizada para atletas de alto rendimiento con periodizacion nutricional.',
    goal: 'Rendimiento',
    target_calories: 3200,
    duration_weeks: 16,
    estimated_cost: 199.99,
    is_active: true,
    created_at: '2024-01-15T10:00:00Z',
    updated_at: '2024-01-15T10:00:00Z'
  }
]

export const nutricionistasEjemplo: Nutricionista[] = [
  {
    id: 1,
    first_name: 'Alexis',
    last_name: 'Paz',
    professional_id: '1723456789',
    specialty: 'Nutricion Deportiva',
    consultation_fee: 45.00,
    consultations_completed: 0,
    is_active: true,
    created_at: '2026-05-27T22:07:23Z',
    updated_at: '2026-05-27T22:07:23Z'
  },
  {
    id: 2,
    first_name: 'Maria',
    last_name: 'Garcia',
    professional_id: '1723456790',
    specialty: 'Nutricion Clinica',
    consultation_fee: 50.00,
    consultations_completed: 45,
    is_active: true,
    created_at: '2024-01-01T10:00:00Z',
    updated_at: '2024-01-01T10:00:00Z'
  }
]

export const consultasEjemplo: ConsultaDietetica[] = [
  {
    id: 1,
    status: 'confirmada',
    session_notes: null,
    scheduled_time: '2026-06-05T10:00:00Z',
    estimated_end: '2026-06-05T11:00:00Z',
    created_at: '2026-05-28T10:00:00Z',
    updated_at: '2026-05-28T10:00:00Z',
    nutricionista_id: 1,
    paciente_id: 1,
    plan_nutricional_id: 1,
    nutricionista: nutricionistasEjemplo[0],
    plan: planesEjemplo[0]
  },
  {
    id: 2,
    status: 'completada',
    session_notes: 'Primera consulta de evaluacion. Se establecieron metas iniciales.',
    scheduled_time: '2026-05-20T14:00:00Z',
    estimated_end: '2026-05-20T15:00:00Z',
    created_at: '2026-05-15T10:00:00Z',
    updated_at: '2026-05-20T15:00:00Z',
    nutricionista_id: 1,
    paciente_id: 1,
    plan_nutricional_id: 1,
    nutricionista: nutricionistasEjemplo[0],
    plan: planesEjemplo[0]
  },
  {
    id: 3,
    status: 'pendiente',
    session_notes: null,
    scheduled_time: '2026-06-12T16:00:00Z',
    estimated_end: '2026-06-12T17:00:00Z',
    created_at: '2026-05-30T10:00:00Z',
    updated_at: '2026-05-30T10:00:00Z',
    nutricionista_id: 2,
    paciente_id: 1,
    plan_nutricional_id: 3,
    nutricionista: nutricionistasEjemplo[1],
    plan: planesEjemplo[2]
  }
]

// Categorias para la pantalla de planes
export const categorias = [
  { id: 'perdida', name: 'Perdida de peso', icon: 'scale', count: 2 },
  { id: 'muscular', name: 'Masa Muscular', icon: 'dumbbell', count: 2 },
  { id: 'saludable', name: 'Saludable', icon: 'salad', count: 4 },
  { id: 'deportivo', name: 'Deportivo', icon: 'running', count: 1 },
  { id: 'especial', name: 'Dietas Especiales', icon: 'leaf', count: 2 }
]
