#ifndef _PCI_2_HPP
#define _PCI_2_HPP

#include <stdint.h>
#include <avr/io.h>
#include "../interrupt_manager.hpp"

template<>
class PinChangeInterrupt<2>
{
    static inline void enable()
    {
        PCICR |= PCIE2;
    }

    static inline void disable()
    {
        PCICR &= ~(1 << PCIE2);
    }

    static inline void attach(void (*user_function)(void))
    {
        InterruptManager::attach(PCINT2_vect_num, user_function);
    }
};

template <uint8_t bit>
class PinChangeInterrupt2
{
public:
    static inline void enable()
    {
        PCMSK2 |= (1 << bit);
        PinChangeInterrupt<2>::enable();
    }

    static inline void disable()
    {
        PCMSK2 &= ~(1 << bit);
    }
};

typedef PinChangeInterrupt<2> pci2;

#ifdef PCINT16
typedef PinChangeInterrupt2<16> pcint16;
#endif
#ifdef PCINT17
typedef PinChangeInterrupt2<17> pcint17;
#endif
#ifdef PCINT18
typedef PinChangeInterrupt2<18> pcint18;
#endif
#ifdef PCINT19
typedef PinChangeInterrupt2<19> pcint19;
#endif
#ifdef PCINT20
typedef PinChangeInterrupt2<20> pcint20;
#endif
#ifdef PCINT21
typedef PinChangeInterrupt2<21> pcint21;
#endif
#ifdef PCINT22
typedef PinChangeInterrupt2<22> pcint22;
#endif
#ifdef PCINT23
typedef PinChangeInterrupt2<23> pcint23;
#endif

#endif // PCI_2_HPP
