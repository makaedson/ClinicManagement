import { useCallback, useEffect, useState } from "react";

async function request(url, options) {
  const response = await fetch(url, options);
  const body = await response.text();

  if (!response.ok) {
    throw new Error(body || `Request failed (${response.status})`);
  }

  return body && (body.startsWith("[") || body.startsWith("{"))
    ? JSON.parse(body)
    : body;
}

function OfficeRegistrationPage({ onSaved }) {
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  async function handleSave(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const formData = new FormData(form);
    const office = {
      name: formData.get("name").trim(),
      officeNumber: formData.get("officeNumber").trim()
    };

    setSaving(true);
    setError("");
    try {
      await request("/api/office/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(office)
      });
      form.reset();
      onSaved();
    } catch (requestError) {
      setError(`Unable to save office: ${requestError.message}`);
    } finally {
      setSaving(false);
    }
  }

  return (
    <section>
      <h2>Register an office</h2>
      <form onSubmit={handleSave}>
        <p>
          <label>
            Office name{" "}
            <input name="name" type="text" maxLength="100" required />
          </label>
        </p>
        <p>
          <label>
            Office number{" "}
            <input name="officeNumber" type="text" maxLength="20" required />
          </label>
        </p>
        {error && <p role="alert">{error}</p>}
        <button type="submit" disabled={saving}>
          {saving ? "Saving..." : "Save"}
        </button>
      </form>
    </section>
  );
}

function OfficeListPage() {
  const [offices, setOffices] = useState([]);
  const [search, setSearch] = useState("");
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [disablingId, setDisablingId] = useState(null);

  const loadOffices = useCallback(async (query = "") => {
    setLoading(true);
    setError("");

    const url = new URL("/api/office/all", window.location.origin);
    if (query.trim()) {
      url.searchParams.set("search", query.trim());
    }

    try {
      setOffices(await request(url));
    } catch (requestError) {
      setError(`Unable to load offices: ${requestError.message}`);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadOffices();
  }, [loadOffices]);

  async function handleDisable(id) {
    setDisablingId(id);
    setError("");
    setMessage("");
    try {
      await request(`/api/office/${encodeURIComponent(id)}/disable`, {
        method: "PUT"
      });
      await loadOffices(search);
      setMessage("Office disabled.");
    } catch (requestError) {
      setError(`Unable to disable office: ${requestError.message}`);
    } finally {
      setDisablingId(null);
    }
  }

  function handleSearch(event) {
    event.preventDefault();
    setMessage("");
    loadOffices(search);
  }

  return (
    <section>
      <h2>Available offices</h2>
      <form onSubmit={handleSearch}>
        <label>
          Search{" "}
          <input
            type="search"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </label>{" "}
        <button type="submit">Search</button>
      </form>

      {error && <p role="alert">{error}</p>}
      {message && <p role="status">{message}</p>}
      {loading ? (
        <p>Loading offices...</p>
      ) : (
        <table>
          <caption>Available offices</caption>
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Office name</th>
              <th scope="col">Office number</th>
              <th scope="col">Status</th>
              <th scope="col">Action</th>
            </tr>
          </thead>
          <tbody>
            {offices.length === 0 ? (
              <tr>
                <td colSpan="5">No offices found.</td>
              </tr>
            ) : (
              offices.map((office, index) => (
                <tr key={office.id}>
                  <th scope="row">{index + 1}</th>
                  <td>{office.name}</td>
                  <td>{office.officeNumber}</td>
                  <td>{office.active ? "Available" : "Disabled"}</td>
                  <td>
                    <button
                      type="button"
                      disabled={!office.active || disablingId === office.id}
                      onClick={() => handleDisable(office.id)}
                    >
                      {!office.active
                        ? "Disabled"
                        : disablingId === office.id
                          ? "Disabling..."
                          : "Disable"}
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      )}
    </section>
  );
}

function currentPage() {
  return window.location.hash === "#/offices" ? "offices" : "register";
}

export default function App() {
  const [page, setPage] = useState(currentPage);

  useEffect(() => {
    function handleLocationChange() {
      setPage(currentPage());
    }

    window.addEventListener("hashchange", handleLocationChange);
    return () => window.removeEventListener("hashchange", handleLocationChange);
  }, []);

  function navigateTo(pageName) {
    window.location.hash = `/${pageName}`;
  }

  return (
    <main>
      <h1>Office management</h1>
      <nav aria-label="Office pages">
        <a href="#/register">Register an office</a>
        {" | "}
        <a href="#/offices">Available offices</a>
      </nav>
      {page === "offices" ? (
        <OfficeListPage />
      ) : (
        <OfficeRegistrationPage onSaved={() => navigateTo("offices")} />
      )}
    </main>
  );
}
