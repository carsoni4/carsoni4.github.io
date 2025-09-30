
library IEEE;
use IEEE.std_logic_1164.all;


entity pc is
	generic (
		N : integer := 32 --number of bits can be adjust easily if needeed
	);


port ( --no we as its always we
i_CLK : in STD_LOGIC;
i_RST : in STD_LOGIC;
i_D : in STD_LOGIC_VECTOR(N-1 downto 0);
o_Q : out STD_LOGIC_VECTOR(N-1 downto 0);
halt: in std_logic
);
end pc;

architecture Structural of pc is 

signal s_RST_Data : std_logic_vector(31 downto 0) := x"00400000"; --instruction init


component pc_dff is
        port(i_CLK : in std_logic;
             i_RST : in std_logic;
             i_D : in std_logic; 
             o_Q : out std_logic;
             i_RST_Data : in std_logic;
             halt : in std_logic
	);
end component;



begin
	gendffs: for i in N-1 downto 0 generate
		dff: pc_dff
			port map(
				i_CLK => i_CLK,
				i_RST => i_RST,
				i_D => i_D(i),
				o_Q => o_Q(i),
                                i_RST_Data => s_RST_Data(i),
                                halt => halt
			);
	end generate;
end Structural;
