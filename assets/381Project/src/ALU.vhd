library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity ALU is
    port (
        A         : in  std_logic_vector(31 downto 0);  -- first register
        B         : in  std_logic_vector(31 downto 0);  -- second register       
        Control   : in  std_logic_vector(3 downto 0);   -- 4-bit control signal
        shamt     : in std_logic_vector(4 downto 0); --shamt
        lui_in    : in std_logic_vector(15 downto 0); --lui immediate
        repl_in   : in std_logic_vector(7 downto 0); --repl
        addsubsigned : in std_logic; --signed when 1 unsigned when 0
        Result    : out std_logic_vector(31 downto 0);  -- Output of ALU
        Zero      : out std_logic;                      -- Flag to indicate if Result is zero
        CarryO    : out std_logic;                      --flag for carry out
        Overflow  : out std_logic;
        jalmux_o  : out std_logic;
	    bne       : in std_logic
    );
end ALU;


architecture dataflow of ALU is --MIGHT NEED TO ADD ARCH HERE
    signal addsub_out_unsigned : std_logic_vector(31 downto 0);  -- Output for add/sub operation
    signal addsub_out_signed : std_logic_vector(31 downto 0);  -- Output for add/sub operation
    signal and_out    : std_logic_vector(31 downto 0);  -- Output for AND operation
    signal or_out     : std_logic_vector(31 downto 0);  -- Output for OR operation
    signal xor_out    : std_logic_vector(31 downto 0);  -- Output for XOR operation
    signal nor_out    : std_logic_vector(31 downto 0);  --Output for NOR
    signal slt_out    : std_logic_vector(31 downto 0);  --Output for SLT
    signal o_shift : std_logic_vector(31 downto 0); --output for shifts
    signal lui_out : std_logic_vector(31 downto 0); --output for lui instruction
    signal repl_out : std_logic_vector(31 downto 0); --output for repl
    signal result_internal : std_logic_vector(31 downto 0); --internal result so I can reed it for zero
    signal i_type : std_logic; --shift type 0 logical 1 a
    signal i_dir : std_logic; --shift direction
    signal overflow_out_sub : std_logic;
    signal overflow_out_add : std_logic;
    signal CarryO_out : std_logic;


    component barrelShifter is
        port(i_X : in std_logic_vector(31 downto 0);
             i_shamt : in std_logic_vector(4 downto 0);
             i_type : in std_logic; -- 0 for logical, 1 for arithmetic
             i_dir : in std_logic; -- 0 for right, 1 for left
             o_Y : out std_logic_vector(31 downto 0));
      end component;



      component addsub is
        generic (N : integer := 32);
        Port(
            A : in STD_LOGIC_VECTOR(N-1 downto 0);
            B : in STD_LOGIC_VECTOR(N-1 downto 0);
            nAdd_Sub : in STD_LOGIC;
            O : out STD_LOGIC_VECTOR(N-1 downto 0);
            O_Carry : out STD_LOGIC
        );
    end component;

begin

    --sets to arithmetic if SRA
    i_type <= '1' when Control = "1001" else '0';
    --sets shift direction to left when sll otherwise right  
    i_dir <= '1' when Control = "1000" else '0';



    -- Instantiating the AddSub component for unsigned addition and subtraction
    unsignedaddsub: addsub
        generic map(N => 32)
        port map (
            A        => A,
            B        => B,
            nAdd_Sub => Control(2), -- Assuming Control(2) determines addition or subtraction (can be changed to entire control)
            O        => addsub_out_unsigned,
            O_Carry  => CarryO_out
        );

        -- signed arithmetic
        addsub_out_signed <= std_logic_vector(signed(A) - signed(B)) when Control(2) = '1' else std_logic_vector(signed(A) + signed(B));
        
        
        --overflow logic       
        overflow_out_add <= '1' when (A(31) = '0' and B(31) = '0' and result_internal(31) = '1') or (A(31) = '1' and B(31) = '1' and result_internal(31) = '0') else '0';
        overflow_out_sub <= '1' when (A(31) = '0' and B(31) = '1' and result_internal(31) = '1') or (A(31) = '1' and B(31) = '0' and result_internal(31) = '0') else '0';

    --shifting logic
    barrel: barrelShifter 
        port map(
            i_X => B,
            i_shamt => shamt,
            i_type => i_type,
            i_dir => i_dir,
            o_Y => o_shift
        );

    
    -- Logic for SLT
    slt_out <= "00000000000000000000000000000001" when signed(A) < signed(B) else
    "00000000000000000000000000000000";
    
    -- Logic for AND operation
    and_out <= A and B;

    -- Logic for OR operation
    or_out <= A or B;

    -- Logic for XOR operation
    xor_out <= A xor B;

    -- Logic for nor
    nor_out <= A nor B;

    --logic for lui
    lui_out <= lui_in & "0000000000000000";

    --logic for repl.qb
    repl_out <= repl_in & repl_in & repl_in & repl_in;


-- ALU Operation Selection based on Control and addsubsigned signal
result_internal <= 
    addsub_out_unsigned when (Control = "0010" and addsubsigned = '0') else
    addsub_out_signed when (Control = "0010" and addsubsigned = '1') else
    addsub_out_unsigned when (Control = "0110" and addsubsigned = '0') else
    addsub_out_signed when (Control = "0110" and addsubsigned = '1') else
    and_out when (Control = "0000") else
    or_out when (Control = "0001") else
    xor_out when (Control = "1111") else
    nor_out when (Control = "1100") else
    slt_out when (Control = "0111") else
    o_shift when (Control = "1000" or Control = "1101" or Control = "1001") else
    lui_out when (Control = "0100") else
    repl_out when (Control = "1110") else
    (others => '0');  -- default


    Result <= result_internal;


    --providing overflow and carryout outputs when the ALU is using addsub, otherwise 0
    Overflow <= '1' when ((Control = "0110" and addsubsigned = '1' and overflow_out_sub = '1') or (Control = "0010" and addsubsigned = '1' and overflow_out_add = '1')) else '0';
    CarryO <= CarryO_out when ((Control = "0110" or Control = "0010") and addsubsigned = '0') else '0';
    -- If zero set zero flag to 1
    Zero <= '1' when ((result_internal = x"00000000" and bne = '0') or (bne = '1' and result_internal /= x"00000000")) else '0';
    
    jalmux_o <= '1' when Control = "1011" else '0';
    
end dataflow;
