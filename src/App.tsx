const projects = [
  {
    title: "AI Integrated Smart Ecosystem (Senior Capstone)",
    description:
      "Built an AI-powered smart aquarium ecosystem that captures live tank images, runs fish detection, stores image/count data, and displays results through a web dashboard.",
    tech: ["Python", "FastAPI", "YOLOv8", ".NET 9", "JWT", "SQLite", "React/Vite, C++, XUnit"],
    github: "https://github.com/carsoni4/SmartEcosystem",
  },
  {
    title: "Supper Solver (COMS 309 Project)",
    description:
      "Developed a full-stack team software project with a structured backend, database integration, and frontend features built around real user workflows.",
    tech: ["Java", "Spring Boot", "JPA/Hibernate", "MySQL"],
    github: "https://github.com/carsoni4/Supper-Solver",
  },
  {
    title: "OMap (Personal Project)",
    description:
      "Personal office mapping project that allows IT teams to create and maintain an interactive map of office layouts, and equipment locations",
    tech: [".NET 10", "React/Vite", "PostgreSQL", "Docker"],
    github: "https://github.com/carsoni4/oMapBackend",
  },
];

const skills = [
  "C#",
  ".NET Core",
  ".NET Framework",
  "SQL Server",
  "React/Vite",
  "REST APIs",
  "Git",
  "TypeScript",
  "WPF",
  "Microsoft Power Apps",
  "Power Automate",
  "Java",
  "Linux"
];

function App() {
  return (
    <main className="min-h-screen bg-slate-950 text-slate-100">
      <Navbar />
      <Hero />
      <About />
      <Experience />
      <Projects />
      <Skills />
      <Contact />
    </main>
  );
}

function Navbar() {
  return (
    <nav className="sticky top-0 z-50 border-b border-white/10 bg-slate-950/80 backdrop-blur">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <a href="#" className="text-lg font-bold tracking-tight">
          Carson Irving
        </a>

        <div className="hidden gap-6 text-sm text-slate-300 md:flex">
          <a className="transition hover:text-sky-400" href="#about">
            About
          </a>
          <a className="transition hover:text-sky-400" href="#experience">
            Experience
          </a>
          <a className="transition hover:text-sky-400" href="#projects">
            Projects
          </a>
          <a className="transition hover:text-sky-400" href="#contact">
            Contact
          </a>
        </div>
      </div>
    </nav>
  );
}

function Hero() {
  return (
    <section className="mx-auto grid max-w-6xl gap-10 px-6 py-24 md:grid-cols-[1.2fr_0.8fr] md:items-center">
      <div>
        <p className="mb-4 text-sm font-semibold uppercase tracking-[0.25em] text-sky-400">
          Software Developer / Cybersecurity Intern
        </p>

        <h1 className="max-w-3xl text-5xl font-bold tracking-tight text-white md:text-7xl">
          Hi, I’m Carson. I build practical software for real business problems.
        </h1>

        <p className="mt-6 max-w-2xl text-lg leading-8 text-slate-300">
          I’m a software-focused IT professional with experience developing
          internal applications, database-backed tools, REST API integrations,
          and business automation solutions.
        </p>

        <div className="mt-8 flex flex-wrap gap-4">
          <a
            href="#projects"
            className="rounded-full bg-sky-400 px-6 py-3 font-semibold text-slate-950 transition hover:bg-sky-300"
          >
            View Projects
          </a>

          <a
            href="#contact"
            className="rounded-full border border-white/20 px-6 py-3 font-semibold text-white transition hover:border-sky-400 hover:text-sky-400"
          >
            Contact Me
          </a>
        </div>
      </div>

      <div className="rounded-3xl border border-white/10 bg-white/5 p-6 shadow-2xl">
        <div className="rounded-2xl bg-slate-900 p-6">
          <p className="text-sm text-slate-400">Currently</p>
          <h2 className="mt-2 text-2xl font-bold text-white">
            Cybersecurity Intern
          </h2>
          <p className="mt-2 text-slate-300">Weiler Products</p>

          <div className="mt-6 space-y-4 text-sm text-slate-300">
            <p>
              <span className="text-sky-400">Location:</span> Knoxville, IA
            </p>
            <p>
              <span className="text-sky-400">Focus:</span> .NET, SQL, WPF,
              REST APIs, Power Apps
            </p>
            <p>
              <span className="text-sky-400">Goal:</span> Building clean,
              useful, maintainable software
            </p>
          </div>
        </div>
      </div>
    </section>
  );
}

function About() {
  return (
    <section id="about" className="border-t border-white/10 bg-slate-900/40">
      <div className="mx-auto max-w-6xl px-6 py-20">
        <SectionHeader eyebrow="About" title="A developer who understands business workflows." />

        <p className="mt-6 max-w-3xl text-lg leading-8 text-slate-300">
          I enjoy building software that helps teams work faster and with fewer
          errors. My experience includes internal application development,
          database work, API integrations, automation, and supporting business
          systems during modernization efforts.
        </p>
      </div>
    </section>
  );
}

function Experience() {
  return (
    <section id="experience">
      <div className="mx-auto max-w-6xl px-6 py-20">
        <SectionHeader eyebrow="Experience" title="Work Experience" />

        <div className="mt-10 rounded-3xl border border-white/10 bg-white/5 p-8">
          <div className="flex flex-col justify-between gap-4 md:flex-row">
            <div>
              <h3 className="text-2xl font-bold text-white">
                Cybersecurity Intern
              </h3>
              <p className="mt-1 text-sky-400">Weiler Products</p>
            </div>

            <p className="text-sm text-slate-400">
              May 2023 – Present · Knoxville, IA
            </p>
          </div>

          <p className="mt-6 leading-7 text-slate-300">
            Weiler Products is a manufacturer of road paving equipment,
            specializing in innovative solutions for the construction industry.
          </p>

          <div className="mt-6 flex flex-wrap gap-2">
            {[".NET Framework", "SQL Server", "WPF", "Power Apps", "CRM REST API", "VBA"].map(
              (item) => (
                <span
                  key={item}
                  className="rounded-full border border-sky-400/30 bg-sky-400/10 px-3 py-1 text-sm text-sky-300"
                >
                  {item}
                </span>
              )
            )}
          </div>

          <ul className="mt-8 space-y-4 text-slate-300">
            <li>
              • Developed SQL Server and CRM REST API-based versions of an
              internal WPF and .NET Framework application.
            </li>
            <li>
              • Supported part description and routing updates during a system
              migration.
            </li>
            <li>
              • Built and maintained business tools using Microsoft Power Apps,
              VBA, and internal data sources.
            </li>
            <li>
              • Collaborated with users to understand workflow needs and turn
              them into practical software improvements.
            </li>
          </ul>
        </div>
      </div>
    </section>
  );
}

function Projects() {
  return (
    <section id="projects" className="border-y border-white/10 bg-slate-900/40">
      <div className="mx-auto max-w-6xl px-6 py-20">
        <SectionHeader eyebrow="Projects" title="Featured Projects" />

        <div className="mt-10 grid gap-6 md:grid-cols-3">
          {projects.map((project) => (
            <article
              key={project.title}
              className="flex flex-col rounded-3xl border border-white/10 bg-white/5 p-6 transition hover:-translate-y-1 hover:border-sky-400/50"
            >
              <h3 className="text-xl font-bold text-white">{project.title}</h3>

              <p className="mt-4 flex-1 leading-7 text-slate-300">
                {project.description}
              </p>

              <div className="mt-6 flex flex-wrap gap-2">
                {project.tech.map((tech) => (
                  <span
                    key={tech}
                    className="rounded-full bg-slate-800 px-3 py-1 text-xs text-slate-300"
                  >
                    {tech}
                  </span>
                ))}
              </div>

              <a
                href={project.github}
                target="_blank"
                rel="noreferrer"
                className="mt-6 inline-flex w-fit rounded-full border border-white/20 px-4 py-2 text-sm font-semibold text-white transition hover:border-sky-400 hover:text-sky-400"
              >
                View GitHub Repo
              </a>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}

function Skills() {
  return (
    <section>
      <div className="mx-auto max-w-6xl px-6 py-20">
        <SectionHeader eyebrow="Skills" title="Technical Skills" />

        <div className="mt-10 flex flex-wrap gap-3">
          {skills.map((skill) => (
            <span
              key={skill}
              className="rounded-full border border-white/10 bg-white/5 px-4 py-2 text-slate-300"
            >
              {skill}
            </span>
          ))}
        </div>
      </div>
    </section>
  );
}

function Contact() {
  return (
    <section id="contact" className="border-t border-white/10 bg-slate-900/40">
      <div className="mx-auto max-w-6xl px-6 py-20">
        <SectionHeader eyebrow="Contact" title="Let’s connect." />

        <p className="mt-6 max-w-2xl text-lg leading-8 text-slate-300">
          I’m interested in software development, IT, cybersecurity, and
          business application roles where I can build useful systems and keep
          learning.
        </p>

        <div className="mt-8 flex flex-wrap gap-4">
          <a
            href="mailto:carsoni@alumni.iastate.edu"
            className="rounded-full bg-sky-400 px-6 py-3 font-semibold text-slate-950 transition hover:bg-sky-300"
          >
            Email Carson
          </a>

          <a
            href="https://github.com/carsoni4"
            target="_blank"
            rel="noreferrer"
            className="rounded-full border border-white/20 px-6 py-3 font-semibold text-white transition hover:border-sky-400 hover:text-sky-400"
          >
            GitHub
          </a>

          <a
            href="https://www.linkedin.com/in/carson-irving-1332a1268/"
            target="_blank"
            rel="noreferrer"
            className="rounded-full border border-white/20 px-6 py-3 font-semibold text-white transition hover:border-sky-400 hover:text-sky-400"
          >
            LinkedIn
          </a>
        </div>
      </div>
    </section>
  );
}

function SectionHeader({
  eyebrow,
  title,
}: {
  eyebrow: string;
  title: string;
}) {
  return (
    <div>
      <p className="text-sm font-semibold uppercase tracking-[0.25em] text-sky-400">
        {eyebrow}
      </p>
      <h2 className="mt-3 text-3xl font-bold tracking-tight text-white md:text-5xl">
        {title}
      </h2>
    </div>
  );
}

export default App;