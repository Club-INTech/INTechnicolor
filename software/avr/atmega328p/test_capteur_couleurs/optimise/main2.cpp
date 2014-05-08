#include <stdint.h>
#include <avr/io.h>
#include <libintech/gpio.hpp>
#include <libintech/uart.hpp>
#include <libintech/timer.hpp>
#include <libintech/isr.hpp>
#include <libintech/capteur_srf05.hpp>
#include <libintech/algorithm.hpp>

typedef ring_buffer<uint16_t, 10> ringBufferC; 
typedef ring_buffer<uint16_t, 8> ringBufferF; 

INITIALISE_INTERRUPT_MANAGER();

ringBufferC ringBufferValeursRG, ringBufferValeursRD, ringBufferValeursBG, ringBufferValeursBD, ringBufferValeursVG, ringBufferValeursVD;
ringBufferF ringBufferValeursFG, ringBufferValeursFD; 
int flag = 0;
uint16_t counterG = 0, counterD = 0, countRG = 0, countRD = 0, countVG = 0,countVD = 0, countBG = 0,countBD = 0, medRG, medRD, medVG, medVD, medBG, medBD, medFG, medFD;

void interruption_timer();
void interruption_int0();
void interruption_int1();

int main() {

  D6::output();
  D5::output();
  D4::output();
  D7::output();
  D2::input();
  D3::input();

  uart0::init();
  uart0::change_baudrate(9600); // vitesse de connection: 9600

  D7::low();
  D4::high();
  D5::high();
  D6::low();

  timer0::mode(timer0::MODE_COUNTER);
  timer0::set_prescaler(timer0::prescaler::PRESCALER_256); //1024 ou 256 ou 64

  timer0::counter::overflow_interrupt::attach(interruption_timer);
  timer0::counter::overflow_interrupt::enable();

  int0::mode(int0::LOW_LEVEL);
  int0::attach(interruption_int0);
  int0::enable();

  int1::mode(int1::LOW_LEVEL);
  int1::attach(interruption_int1);
  int1::enable();

  char buffer[17];  

  while(1){
    uart0::read(buffer);
    if(!strcmp(buffer,"?")){
      
      ringBufferValeursRG.append(countRG);
      medRG = mediane(ringBufferValeursRG);

      ringBufferValeursRD.append(countRD);
      medRD = mediane(ringBufferValeursRD);

      ringBufferValeursBG.append(countBG);
      medBG = mediane(ringBufferValeursBG);

      ringBufferValeursBD.append(countBD);
      medBD = mediane(ringBufferValeursBD);

      ringBufferValeursVG.append(countVG);
      medVG = mediane(ringBufferValeursVG);

      ringBufferValeursVD.append(countVD);
      medVD = mediane(ringBufferValeursVD);

      if(medRG/medVG == 0){
	ringBufferValeursFG.append(0);
	//	uart0::printfln("ROUGE g=%d b=%d r=%d et ",medVG,medBG,medRG);
	medFG = mediane(ringBufferValeursFG);
      }
      else{
	if(medRG/medVG == 1){
	ringBufferValeursFG.append(2);
      //	uart0::printfln("JAUNE g=%d b=%d r=%d et ",medVG,medBG,medRG);
	medFG = mediane(ringBufferValeursFG);
	}
	else{
	ringBufferValeursFG.append(1);
      //	uart0::printfln("? g=%d %d r=%d et",medVG,medBG,medRG);
	medFG = mediane(ringBufferValeursFG);
	}
      }
      if(medFG == 0)
	uart0::printfln("ROUGE  et ");
      else{
	if(medFG == 2)
	  uart0::printfln("JAUNE  et ");
	else
	  uart0::printfln("? et ");
      }

      if(medRD/medVD == 0){
	//uart0::printfln("ROUGE g=%d b=%d r=%d\n",medVD,medBD,medRD);
	ringBufferValeursFD.append(1);
	medFD = mediane(ringBufferValeursFD);
      }
      else{
	if(medRD/medVD == 1){
	  //uart0::printfln("JAUNE g=%d b=%d r=%d\n",medVD,medBD,medRD);
	  ringBufferValeursFD.append(2);
	  medFD = mediane(ringBufferValeursFD);
	}
	else{
	  //uart0::printfln("? g=%d %d r=%d\n",medVD,medBD,medRD);
	  ringBufferValeursFD.append(0);
	  medFD = mediane(ringBufferValeursFD);
	}
      }
      if(medFD == 0)
	uart0::printfln("ROUGE\n");
      else{
	if(medFD == 2)
	  uart0::printfln("JAUNE\n");
	else
	  uart0::printfln("?\n");
      } 
    }
  }
}

void interruption_timer()
{

	flag++;
	if(flag == 1)
	{
		countRG = counterG;
		countRD = counterD;
		D4::high(); 
		D7::high();

	}
	else if(flag == 2)
	{

		countVG = counterG;
		countVD = counterD;
		D4::low();
		D7::high();

	}
	else if(flag == 3)
	{

		countBG = counterG;
		countBD = counterD;
		D4::low();
		D7::low();
	}
	else if(flag == 4)
	{

		flag = 0;
	}
	counterG = 0;
	counterD = 0;
}

void interruption_int0()
{
  counterG++;
}

void interruption_int1(){

  counterD++;
}

/*Cablage: s0 = D6
           s1 = D5
           s2 = D4
           s3 = D7
           out = D2 et D3

Valeurs: RIEN: r=14 b=11/12 g=13
         JAUNE: r=13/17 b=12 g=11
         ROUGE: r=15 b=14/7 g=17
*/ 
