library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity fulladder is

	Port(
		i_A : in STD_LOGIC;
		i_B : in STD_LOGIC;
		C_in : in STD_LOGIC;
		S : out STD_LOGIC;
		C_out : out STD_LOGIC
	);
end fulladder;


architecture Structural of fulladder is 

component xorg2 is
	Port (
	i_A : in STD_LOGIC;
	i_B : in STD_LOGIC;
	o_F : out STD_LOGIC
	);
end component;

component andg2 is
	Port (
	i_A : in STD_LOGIC;
	i_B : in STD_LOGIC;
	o_F : out STD_LOGIC
	);
end component;

component org2 is 
	Port(
	i_A : in STD_LOGIC;
	i_B : in STD_LOGIC;
	o_F : out STD_LOGIC
	);
end component;

signal xor1_out : STD_LOGIC;
signal and1_out : STD_LOGIC;
signal and2_out : STD_LOGIC;

begin

	xor1: xorg2 port map (
		i_A => i_A,
		i_B => i_B,
		o_F => xor1_out
		);
	xor2: xorg2 port map(
		i_A => xor1_out,
		i_B => C_in,
		o_F => S
		);
	and1: andg2 port map(
		i_A => xor1_out,
		i_B => C_in,
		o_F => and1_out
		);
	and2: andg2 port map(
		i_A => i_A,
		i_B => i_B,
		o_F => and2_out
		);
	or1: org2 port map(
		i_A => and1_out,
		i_B => and2_out,
		o_F => C_out
		);


end Structural;