'use client'

import { useState } from 'react'
import { Calendar, Plus, Clock, User, Video, CheckCircle2, Clock4, XCircle, FileText, ChevronRight } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { nutricionistasEjemplo, planesEjemplo, consultasEjemplo } from '@/lib/mock-data'
import { useToast } from '@/hooks/use-toast'
import { cn } from '@/lib/utils'
import type { ConsultaDietetica } from '@/lib/types'

const statusConfig = {
  pendiente: { 
    label: 'Pendiente', 
    icon: Clock4, 
    bg: 'bg-amber-50', 
    text: 'text-amber-700',
    dot: 'bg-amber-400'
  },
  confirmada: { 
    label: 'Confirmada', 
    icon: CheckCircle2, 
    bg: 'bg-emerald-50', 
    text: 'text-emerald-700',
    dot: 'bg-emerald-400'
  },
  completada: { 
    label: 'Completada', 
    icon: CheckCircle2, 
    bg: 'bg-blue-50', 
    text: 'text-blue-700',
    dot: 'bg-blue-400'
  },
  cancelada: { 
    label: 'Cancelada', 
    icon: XCircle, 
    bg: 'bg-rose-50', 
    text: 'text-rose-700',
    dot: 'bg-rose-400'
  },
}

function formatDate(dateString: string) {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES', { 
    weekday: 'short',
    day: 'numeric', 
    month: 'short'
  })
}

function formatTime(dateString: string) {
  const date = new Date(dateString)
  return date.toLocaleTimeString('es-ES', { 
    hour: '2-digit', 
    minute: '2-digit'
  })
}

function ConsultaCard({ consulta }: { consulta: ConsultaDietetica }) {
  const status = statusConfig[consulta.status]
  const StatusIcon = status.icon

  return (
    <Card className="group overflow-hidden border-0 shadow-sm transition-all duration-300 hover:shadow-md">
      <CardContent className="p-4">
        <div className="flex gap-4">
          {/* Date badge */}
          <div className="flex h-14 w-14 flex-shrink-0 flex-col items-center justify-center rounded-2xl bg-primary/10">
            <span className="text-[10px] font-semibold uppercase text-primary">
              {new Date(consulta.scheduled_time).toLocaleDateString('es-ES', { month: 'short' })}
            </span>
            <span className="text-xl font-bold text-primary">
              {new Date(consulta.scheduled_time).getDate()}
            </span>
          </div>

          {/* Content */}
          <div className="flex-1 min-w-0">
            <div className="flex items-start justify-between gap-2">
              <div>
                <h3 className="font-semibold text-foreground">
                  {consulta.nutricionista?.first_name} {consulta.nutricionista?.last_name}
                </h3>
                <p className="text-xs text-muted-foreground">{consulta.nutricionista?.specialty}</p>
              </div>
              <span className={cn(
                'flex items-center gap-1 rounded-full px-2 py-0.5 text-[10px] font-semibold',
                status.bg, status.text
              )}>
                <span className={cn('h-1.5 w-1.5 rounded-full', status.dot)} />
                {status.label}
              </span>
            </div>

            <div className="mt-3 flex items-center gap-3 text-xs text-muted-foreground">
              <span className="flex items-center gap-1">
                <Clock className="h-3.5 w-3.5" />
                {formatTime(consulta.scheduled_time)}
              </span>
              <span className="flex items-center gap-1">
                <Video className="h-3.5 w-3.5" />
                Videollamada
              </span>
            </div>

            {consulta.plan && (
              <div className="mt-2 flex items-center gap-1 text-xs">
                <FileText className="h-3.5 w-3.5 text-muted-foreground" />
                <span className="text-muted-foreground">Plan:</span>
                <span className="font-medium text-foreground">{consulta.plan.name}</span>
              </div>
            )}

            {consulta.session_notes && (
              <p className="mt-2 text-xs text-muted-foreground line-clamp-2 italic">
                &quot;{consulta.session_notes}&quot;
              </p>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

export function ConsultasPage() {
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [activeTab, setActiveTab] = useState<'proximas' | 'historial'>('proximas')
  const { toast } = useToast()

  const consultasPendientes = consultasEjemplo.filter(c => c.status === 'pendiente' || c.status === 'confirmada')
  const consultasCompletadas = consultasEjemplo.filter(c => c.status === 'completada')

  const handleAgendarConsulta = () => {
    setIsDialogOpen(false)
    toast({
      title: 'Consulta agendada',
      description: 'Tu consulta ha sido agendada exitosamente.',
    })
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="sticky top-0 z-40 bg-card/80 backdrop-blur-xl">
        <div className="flex items-center justify-between px-5 py-4">
          <div>
            <h1 className="text-2xl font-bold tracking-tight text-foreground">Mis Consultas</h1>
            <p className="text-sm text-muted-foreground">Gestiona tus citas</p>
          </div>
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <Button size="sm" className="h-10 rounded-xl gap-1.5 font-semibold shadow-lg shadow-primary/25">
                <Plus className="h-4 w-4" />
                Nueva
              </Button>
            </DialogTrigger>
            <DialogContent className="mx-4 rounded-2xl">
              <DialogHeader>
                <DialogTitle className="text-xl">Agendar Consulta</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 pt-2">
                <div className="space-y-2">
                  <Label className="text-sm font-medium">Nutricionista</Label>
                  <Select>
                    <SelectTrigger className="h-12 rounded-xl">
                      <SelectValue placeholder="Selecciona un nutricionista" />
                    </SelectTrigger>
                    <SelectContent>
                      {nutricionistasEjemplo.map((n) => (
                        <SelectItem key={n.id} value={n.id.toString()}>
                          <div className="flex items-center gap-2">
                            <User className="h-4 w-4 text-muted-foreground" />
                            {n.first_name} {n.last_name}
                          </div>
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label className="text-sm font-medium">Plan (opcional)</Label>
                  <Select>
                    <SelectTrigger className="h-12 rounded-xl">
                      <SelectValue placeholder="Selecciona un plan" />
                    </SelectTrigger>
                    <SelectContent>
                      {planesEjemplo.map((p) => (
                        <SelectItem key={p.id} value={p.id.toString()}>
                          {p.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label className="text-sm font-medium">Fecha y hora</Label>
                  <Input type="datetime-local" className="h-12 rounded-xl" />
                </div>
                <Button className="h-12 w-full rounded-xl font-semibold" onClick={handleAgendarConsulta}>
                  Confirmar Cita
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        {/* Tabs */}
        <div className="flex gap-2 px-5 pb-4">
          <button
            onClick={() => setActiveTab('proximas')}
            className={cn(
              'flex-1 rounded-xl py-2.5 text-sm font-semibold transition-all duration-200',
              activeTab === 'proximas' 
                ? 'bg-primary text-primary-foreground shadow-lg shadow-primary/25' 
                : 'bg-secondary text-secondary-foreground'
            )}
          >
            Proximas ({consultasPendientes.length})
          </button>
          <button
            onClick={() => setActiveTab('historial')}
            className={cn(
              'flex-1 rounded-xl py-2.5 text-sm font-semibold transition-all duration-200',
              activeTab === 'historial' 
                ? 'bg-primary text-primary-foreground shadow-lg shadow-primary/25' 
                : 'bg-secondary text-secondary-foreground'
            )}
          >
            Historial ({consultasCompletadas.length})
          </button>
        </div>
      </header>

      <main className="px-5 pb-8">
        {activeTab === 'proximas' && (
          <>
            {consultasPendientes.length > 0 ? (
              <div className="space-y-3 py-4">
                {consultasPendientes.map((consulta) => (
                  <ConsultaCard key={consulta.id} consulta={consulta} />
                ))}
              </div>
            ) : (
              <div className="flex flex-col items-center justify-center py-16">
                <div className="mb-5 flex h-20 w-20 items-center justify-center rounded-full bg-secondary">
                  <Calendar className="h-9 w-9 text-muted-foreground" />
                </div>
                <h3 className="mb-1 text-lg font-bold text-foreground">No tienes consultas</h3>
                <p className="mb-6 text-center text-sm text-muted-foreground max-w-xs">
                  Agenda una consulta con nuestros nutricionistas expertos
                </p>
                <Button 
                  onClick={() => setIsDialogOpen(true)}
                  className="h-11 rounded-xl gap-1.5 font-semibold shadow-lg shadow-primary/25"
                >
                  <Plus className="h-4 w-4" />
                  Agendar consulta
                </Button>
              </div>
            )}
          </>
        )}

        {activeTab === 'historial' && (
          <>
            {consultasCompletadas.length > 0 ? (
              <div className="space-y-3 py-4">
                {consultasCompletadas.map((consulta) => (
                  <ConsultaCard key={consulta.id} consulta={consulta} />
                ))}
              </div>
            ) : (
              <div className="flex flex-col items-center justify-center py-16">
                <div className="mb-5 flex h-20 w-20 items-center justify-center rounded-full bg-secondary">
                  <Calendar className="h-9 w-9 text-muted-foreground" />
                </div>
                <h3 className="mb-1 text-lg font-bold text-foreground">Sin historial</h3>
                <p className="text-center text-sm text-muted-foreground">
                  Aqui apareceran tus consultas completadas
                </p>
              </div>
            )}
          </>
        )}
      </main>
    </div>
  )
}
