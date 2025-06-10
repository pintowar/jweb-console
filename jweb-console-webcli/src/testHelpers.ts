function setupMockFetch(data: any, status = 200, contentBody = "json") {
  const fetchMock = vi.fn().mockResolvedValue({
    ok: status < 400,
    [contentBody]: () => Promise.resolve(data),
  });
  globalThis.fetch = fetchMock;

  return fetchMock;
}

export { setupMockFetch };
