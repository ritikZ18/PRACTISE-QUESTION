const svgToDataUri = (svg: string) =>
  `data:image/svg+xml;charset=utf-8,${encodeURIComponent(svg)}`;

export const posterFallbackDataUri = (title: string) => {
  const safeTitle = (title || 'Movie').slice(0, 24);
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="342" height="513" viewBox="0 0 342 513">
      <defs>
        <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0" stop-color="#141826"/>
          <stop offset="1" stop-color="#0b0d14"/>
        </linearGradient>
        <radialGradient id="r" cx="30%" cy="20%" r="80%">
          <stop offset="0" stop-color="rgba(255,255,255,0.14)"/>
          <stop offset="1" stop-color="rgba(255,255,255,0)"/>
        </radialGradient>
      </defs>
      <rect width="342" height="513" fill="url(#g)"/>
      <rect width="342" height="513" fill="url(#r)"/>
      <circle cx="54" cy="52" r="10" fill="#e11d48" opacity="0.65"/>
      <text x="24" y="112" fill="rgba(255,255,255,0.92)" font-size="22" font-family="ui-sans-serif,system-ui,-apple-system,Segoe UI,Roboto,Arial" font-weight="800">
        ${safeTitle.replace(/&/g, '&amp;').replace(/</g, '&lt;')}
      </text>
      <text x="24" y="144" fill="rgba(255,255,255,0.6)" font-size="14" font-family="ui-sans-serif,system-ui,-apple-system,Segoe UI,Roboto,Arial" font-weight="600">
        Poster unavailable
      </text>
    </svg>
  `;
  return svgToDataUri(svg);
};

