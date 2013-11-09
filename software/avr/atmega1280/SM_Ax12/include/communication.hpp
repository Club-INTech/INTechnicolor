#ifndef COMMUNICATION_H
#define COMMUNICATION_H

#include <libintech/ax12.hpp>
#include <libintech/uart.hpp>
#include <util/delay.h>


class Communication
{
	public:

	typedef uart0 serial_pc;
	typedef uart1 serial_ax12;
	typedef AX<serial_ax12> Ax12;
	Ax12 ax12;
	
	public:

	Communication():
		ax12(0,1,1023)
	{
		serial_pc::init();
		serial_pc::change_baudrate(9600);
		serial_ax12::init();
		serial_ax12::change_baudrate(9600);
	}
	void execute(char ordre[])
	{
		if (strcmp(ordre,"?") == 0)
		{
			serial_pc::printfln("%d", 3);
		}
		else if (strcmp(ordre,"angle") == 0)
		{
			uint16_t t;
			serial_pc::read(t);
			ax12.goTo(t);
		}
		else if (strcmp(ordre,"pompe") == 0)
		{
			uint8_t i;
			ax12.goTo(90);
			_delay_ms(750);
			ax12.goTo(250);
			_delay_ms(750);
		}
	}
};

#endif

