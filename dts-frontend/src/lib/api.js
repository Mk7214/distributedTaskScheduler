const BASE_URL = 'http://localhost:8080';

async function handleResponse(response) {
    if (!response.ok) {
        const text = await response.text();
        throw new Error(text || `HTTP error! status: ${response.status}`);
    }
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        return response.json();
    }
    return response.text();
}

export const api = {
    // Scheduler Endpoints
    scheduler: {
        start: () => fetch(`${BASE_URL}/scheduler/start`, { method: 'POST' }).then(handleResponse),
        stop: (id) => fetch(`${BASE_URL}/scheduler/stop/${id}`, { method: 'POST' }).then(handleResponse),
        getLogs: () => fetch(`${BASE_URL}/scheduler/logs`).then(handleResponse),
        getLogContent: (fileName) => fetch(`${BASE_URL}/scheduler/logs/${fileName}`).then(handleResponse),
    },

    // Worker Endpoints
    worker: {
        start: () => fetch(`${BASE_URL}/worker/start`, { method: 'POST' }).then(handleResponse),
        stop: (id) => fetch(`${BASE_URL}/worker/stop/${id}`, { method: 'POST' }).then(handleResponse),
        getLogs: () => fetch(`${BASE_URL}/worker/logs`).then(handleResponse),
        getLogContent: (fileName) => fetch(`${BASE_URL}/worker/logs/${fileName}`).then(handleResponse),
    },

    // Task Endpoints
    tasks: {
        create: (taskData) => fetch(`${BASE_URL}/tasks`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(taskData),
        }).then(handleResponse),
        getAll: (page = 0, size = 10) => fetch(`${BASE_URL}/tasks?page=${page}&size=${size}`).then(handleResponse),
        getCompleted: (page = 0, size = 10) => fetch(`${BASE_URL}/tasks/completed?page=${page}&size=${size}`).then(handleResponse),
        getDead: (page = 0, size = 10) => fetch(`${BASE_URL}/tasks/deadTasks?page=${page}&size=${size}`).then(handleResponse),
    }
};
