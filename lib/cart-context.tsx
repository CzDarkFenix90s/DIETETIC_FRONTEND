'use client'

import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import type { CartItem, PlanNutricional } from './types'

interface CartContextType {
  items: CartItem[]
  addToCart: (plan: PlanNutricional) => void
  removeFromCart: (planId: number) => void
  clearCart: () => void
  getTotal: () => number
  itemCount: number
}

const CartContext = createContext<CartContextType | undefined>(undefined)

export function CartProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<CartItem[]>([])

  const addToCart = useCallback((plan: PlanNutricional) => {
    setItems(prev => {
      const existing = prev.find(item => item.plan.id === plan.id)
      if (existing) {
        return prev.map(item =>
          item.plan.id === plan.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      }
      return [...prev, { plan, quantity: 1 }]
    })
  }, [])

  const removeFromCart = useCallback((planId: number) => {
    setItems(prev => prev.filter(item => item.plan.id !== planId))
  }, [])

  const clearCart = useCallback(() => {
    setItems([])
  }, [])

  const getTotal = useCallback(() => {
    return items.reduce((total, item) => total + (item.plan.estimated_cost * item.quantity), 0)
  }, [items])

  const itemCount = items.reduce((count, item) => count + item.quantity, 0)

  return (
    <CartContext.Provider value={{ items, addToCart, removeFromCart, clearCart, getTotal, itemCount }}>
      {children}
    </CartContext.Provider>
  )
}

export function useCart() {
  const context = useContext(CartContext)
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider')
  }
  return context
}
