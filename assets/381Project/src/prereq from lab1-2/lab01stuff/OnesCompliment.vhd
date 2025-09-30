library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity ones_compliment is
	generic ( N : integer := 32 );
	Port(
		i_N : in STD_LOGIC_VECTOR(N-1 downto 0);
		o_Count : out STD_LOGIC_VECTOR(N-1 downto 0)
	);
end ones_compliment;


architecture Structural of ones_compliment is 

--importing not
component invg
	Port (
	i_A : in STD_LOGIC;
	o_F : out STD_LOGIC

	);
end component;

--invert
signal invert : std_logic_vector(N-1 downto 0);


begin


--making not gates for each bit
not_bits: for i in 0 to N-1 generate
	inv: invg port map (
		i_A => i_N(i),
		o_F => invert(i)
		);
end generate;


o_Count <= invert;

end Structural;