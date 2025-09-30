library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity extender is
    port (
        input_16bit  : in  std_logic_vector(15 downto 0);  -- 16-bit input value
        mode         : in  std_logic;                      -- '1' for sign extension, '0' for zero extension
        output_32bit : out std_logic_vector(31 downto 0)   -- 32-bit extended output value
    );
end entity extender;

architecture behavioral of extender is
begin
    process(input_16bit, mode)
    begin
        if mode = '1' then
            -- Sign extension: replicate the sign bit (MSB of input_16bit) to the upper 16 bits
            output_32bit <= (15 downto 0 => input_16bit(15)) & input_16bit;
        else
            -- Zero extension: fill the upper 16 bits with '0'
            output_32bit <= (15 downto 0 => '0') & input_16bit;
        end if;
    end process;
end architecture behavioral;

