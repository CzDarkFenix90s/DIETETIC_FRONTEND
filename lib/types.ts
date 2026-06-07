// Tipos basados en tu esquema de PostgreSQL

export interface Paciente {
  id: number
  patient_code: string
  full_name: string
  age: number
  goal: string
  dietary_restrictions: string | null
  current_weight: number
  height_cm: number
  status: string
  medical_notes: string | null
  created_at: string
  updated_at: string
}

export interface Nutricionista {
  id: number
  first_name: string
  last_name: string
  professional_id: string
  specialty: string
  consultation_fee: number
  consultations_completed: number
  is_active: boolean
  created_at: string
  updated_at: string
}

export interface PlanNutricional {
  id: number
  name: string
  description: string
  goal: string
  target_calories: number
  duration_weeks: number
  estimated_cost: number
  is_active: boolean
  created_at: string
  updated_at: string
}

export interface ConsultaDietetica {
  id: number
  status: 'pendiente' | 'confirmada' | 'completada' | 'cancelada'
  session_notes: string | null
  scheduled_time: string
  estimated_end: string
  created_at: string
  updated_at: string
  nutricionista_id: number
  paciente_id: number
  plan_nutricional_id: number | null
  nutricionista?: Nutricionista
  plan?: PlanNutricional
}

export interface AlimentoProgramado {
  id: number
  name: string
  description: string
  portion_grams: number
  meal_type: 'desayuno' | 'almuerzo' | 'cena' | 'snack'
  sequence: number
  is_active: boolean
  created_at: string
  updated_at: string
  plan_nutricional_id: number
}

export interface SeguimientoNutricional {
  id: number
  weight_kg: number
  waist_cm: number
  notes: string | null
  created_at: string
  paciente_id: number
}

export interface CartItem {
  plan: PlanNutricional
  quantity: number
}
