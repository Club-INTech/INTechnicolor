#include <stdint.h>
#include <util/delay.h>
#include <libintech/serial/serial_0_interrupt.hpp>
#include <libintech/serial/serial_0.hpp>
#include <libintech/timer.hpp>
#include <libintech/gpio.hpp>


int main(){
	timer0::mode(timer0::MODE_PWM);
	timer0::prescaler(timer0::PRESCALER_8);

	timer0::pwm::waveform_mode(timer0::pwm::PWM_FAST);
	timer0::pwm::output_mode_b(timer0::pwm::OUTPUT_NON_INVERTING);
	timer0::pwm::pwm_b(20);

	D5::output();

	while(1){

		D5::toggle();
	}
	return 0;
}
