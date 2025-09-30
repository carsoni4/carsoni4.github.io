
library IEEE;
use IEEE.std_logic_1164.all;


entity nbitreg is
	generic (
		N : integer := 32 --number of bits can be adjust easily if needeed
	);


port (
i_CLK : in STD_LOGIC;
i_RST : in STD_LOGIC;
i_WE : in STD_LOGIC;
i_D : in STD_LOGIC_VECTOR(N-1 downto 0);
o_Q : out STD_LOGIC_VECTOR(N-1 downto 0)

);
end nbitreg;

architecture Structural of nbitreg is 

component dffg is
        port(i_CLK : in std_logic;
             i_RST : in std_logic;
             i_WE : in std_logic; 
             i_D : in std_logic; 
             o_Q : out std_logic
	);
end component;




begin
	gendffs: for i in N-1 downto 0 generate
		dff: dffg
			port map(
				i_CLK => i_CLK,
				i_RST => i_RST,
				i_WE => i_WE,
				i_D => i_D(i),
				o_Q => o_Q(i)
			);
	end generate;
end Structural;
