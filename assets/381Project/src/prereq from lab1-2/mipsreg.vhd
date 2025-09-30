library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity mipsreg is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        read_reg1  : in  STD_LOGIC_VECTOR (4 downto 0);
        read_reg2  : in  STD_LOGIC_VECTOR (4 downto 0);
        write_reg  : in  STD_LOGIC_VECTOR (4 downto 0);
        write_data : in  STD_LOGIC_VECTOR (31 downto 0);
        read_data1 : out STD_LOGIC_VECTOR (31 downto 0);
        read_data2 : out STD_LOGIC_VECTOR (31 downto 0);
        halt  	   : in std_logic
    );
end mipsreg;

architecture Behavioral of mipsreg is
    type reg_array is array (0 to 31) of STD_LOGIC_VECTOR (31 downto 0);
    signal registers : reg_array := (others => (others => '0'));
begin
    process(clk)
    begin
        if rising_edge(clk) then
            if i_RST = '1' then
                -- Reset all registers except $0
                for i in 1 to 31 loop
                    registers(i) <= (others => '0');
                end loop;
            elsif reg_write = '1' and write_reg /= "00000" and halt = '0' then
                registers(to_integer(unsigned(write_reg))) <= write_data;
            end if;
        end if;
    end process;

    read_data1 <= registers(to_integer(unsigned(read_reg1)));
    read_data2 <= registers(to_integer(unsigned(read_reg2)));
end Behavioral;

