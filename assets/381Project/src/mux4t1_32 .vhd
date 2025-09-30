
library IEEE;
use IEEE.std_logic_1164.all;

entity mux4t1_32 is
  port(i_S          : in std_logic_vector(1 downto 0);
       i_D0         : in std_logic_vector(31 downto 0);
       i_D1         : in std_logic_vector(31 downto 0);
       i_D2         : in std_logic_vector(31 downto 0);
       i_D3         : in std_Logic_vector(31 downto 0);
       o_O          : out std_logic_vector(31 downto 0));
end mux4t1_32;

architecture structural of mux4t1_32 is
begin

o_O <= i_D1 when i_S = "01"
else   i_D2 when i_S = "10"
else   i_D3 when i_S = "11"
else   i_D0; --zero condition
  
end structural;
