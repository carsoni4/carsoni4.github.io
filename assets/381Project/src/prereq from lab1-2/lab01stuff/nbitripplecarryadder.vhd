library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity ripple_carry_adder is
	
	generic(N : integer := 32); --BIT VALUE

	Port(
		i_A : in STD_LOGIC_VECTOR(N-1 downto 0);
		i_B : in STD_LOGIC_VECTOR(N-1 downto 0);
		C_in : in STD_LOGIC;
		S : out STD_LOGIC_VECTOR(N-1 downto 0);
		C_out : out STD_LOGIC
	);


end ripple_carry_adder;


architecture Structural of ripple_carry_adder is 

component fulladder is
	Port(
	i_A : in STD_LOGIC;
	i_B : in STD_LOGIC;
	C_in : in STD_LOGIC;
	S : out STD_LOGIC;
	C_out : out STD_LOGIC
	);
end component;

signal carry : STD_LOGIC_VECTOR(N downto 0);

begin 
	carry(0) <= C_in;
	

	--Create N full adders
	create_fulladders: for i in 0 to N-1 generate
		fulladders: fulladder port map (
				i_A => i_A(i),
				i_B => i_B(i),
				C_in => carry(i),
				S => S(i),
				C_out => carry(i+1)
				);
	end generate;


	C_out <= carry(N);

end Structural;