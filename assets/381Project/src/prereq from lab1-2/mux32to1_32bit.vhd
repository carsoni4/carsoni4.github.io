library IEEE;
use IEEE.std_logic_1164.all;

entity mux32to1_32bit is
    port (
        i_select  : in  std_logic_vector(4 downto 0);    -- 5-bit select signal
        i_inputs  : in  std_logic_vector(1023 downto 0); -- 32 inputs, each 32 bits (1024 bits in total)
        o_output  : out std_logic_vector(31 downto 0)    -- 32-bit output
    );
end mux32to1_32bit;

architecture Dataflow of mux32to1_32bit is
begin
    -- Decode the 5-bit select signal to choose one of the 32 32-bit input buses
    with i_select select
        o_output <= i_inputs(31 downto 0)     when "00000", -- Input 0
                    i_inputs(63 downto 32)    when "00001", -- Input 1
                    i_inputs(95 downto 64)    when "00010", -- Input 2
                    i_inputs(127 downto 96)   when "00011", -- Input 3
                    i_inputs(159 downto 128)  when "00100", -- Input 4
                    i_inputs(191 downto 160)  when "00101", -- Input 5
                    i_inputs(223 downto 192)  when "00110", -- Input 6
                    i_inputs(255 downto 224)  when "00111", -- Input 7
                    i_inputs(287 downto 256)  when "01000", -- Input 8
                    i_inputs(319 downto 288)  when "01001", -- Input 9
                    i_inputs(351 downto 320)  when "01010", -- Input 10
                    i_inputs(383 downto 352)  when "01011", -- Input 11
                    i_inputs(415 downto 384)  when "01100", -- Input 12
                    i_inputs(447 downto 416)  when "01101", -- Input 13
                    i_inputs(479 downto 448)  when "01110", -- Input 14
                    i_inputs(511 downto 480)  when "01111", -- Input 15
                    i_inputs(543 downto 512)  when "10000", -- Input 16
                    i_inputs(575 downto 544)  when "10001", -- Input 17
                    i_inputs(607 downto 576)  when "10010", -- Input 18
                    i_inputs(639 downto 608)  when "10011", -- Input 19
                    i_inputs(671 downto 640)  when "10100", -- Input 20
                    i_inputs(703 downto 672)  when "10101", -- Input 21
                    i_inputs(735 downto 704)  when "10110", -- Input 22
                    i_inputs(767 downto 736)  when "10111", -- Input 23
                    i_inputs(799 downto 768)  when "11000", -- Input 24
                    i_inputs(831 downto 800)  when "11001", -- Input 25
                    i_inputs(863 downto 832)  when "11010", -- Input 26
                    i_inputs(895 downto 864)  when "11011", -- Input 27
                    i_inputs(927 downto 896)  when "11100", -- Input 28
                    i_inputs(959 downto 928)  when "11101", -- Input 29
                    i_inputs(991 downto 960)  when "11110", -- Input 30
                    i_inputs(1023 downto 992) when others;  -- Input 31
end Dataflow;
