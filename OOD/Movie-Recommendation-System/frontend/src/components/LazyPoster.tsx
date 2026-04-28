import React, { useEffect, useMemo, useState } from 'react';
import { api } from '../api';
import { useInView } from '../hooks/useInView';
import { posterFallbackDataUri } from './PosterFallback';
import './LazyPoster.css';

interface LazyPosterProps {
  posterUrl: string;
  title: string;
  year?: number;
  language?: string;
  className?: string;
}

const resolvePosterUrl = (posterUrl: string, title: string) => {
  const raw = (posterUrl || '').trim();
  if (!raw) return posterFallbackDataUri(title);
  if (raw.includes('via.placeholder.com')) return posterFallbackDataUri(title);
  if (raw.startsWith('http://') || raw.startsWith('https://')) return raw;
  if (raw.startsWith('/')) return `https://image.tmdb.org/t/p/w342${raw}`;
  return raw;
};

const posterLookupCache = new Map<string, Promise<string> | string>();

const normalizeTmdbLanguage = (value?: string) => {
  const raw = (value || '').trim().toLowerCase();
  if (!raw) return 'en-US';
  if (raw === 'english' || raw === 'en') return 'en-US';
  if (raw.length === 2) {
    const map: Record<string, string> = {
      hi: 'hi-IN',
      ja: 'ja-JP',
      ko: 'ko-KR',
      fr: 'fr-FR',
      es: 'es-ES',
      ru: 'ru-RU',
      zh: 'zh-CN',
      de: 'de-DE',
      it: 'it-IT',
      pt: 'pt-BR',
    };
    return map[raw] || 'en-US';
  }
  if (/^[a-z]{2}-[a-z]{2}$/i.test(raw)) return raw;
  return 'en-US';
};

const cacheKey = (title: string, year?: number, language?: string) =>
  `${(title || '').trim().toLowerCase()}|${year || ''}|${(language || 'en-US').toLowerCase()}`;

export const LazyPoster: React.FC<LazyPosterProps> = ({ posterUrl, title, year, language, className }) => {
  const { ref, isInView } = useInView<HTMLDivElement>({ rootMargin: '350px 0px', threshold: 0.01 });
  const [hasError, setHasError] = useState(false);
  const [resolvedUrl, setResolvedUrl] = useState<string>('');

  const src = useMemo(() => resolvePosterUrl(posterUrl, title), [posterUrl, title]);
  const fallback = useMemo(() => posterFallbackDataUri(title), [title]);

  useEffect(() => {
    let cancelled = false;
    const shouldLookup = isInView && (!posterUrl || posterUrl.trim() === '');
    if (!shouldLookup) return;
    if (resolvedUrl) return;

    (async () => {
      try {
        const lookupLang = normalizeTmdbLanguage(language);
        const key = cacheKey(title, year, lookupLang);
        const cached = posterLookupCache.get(key);
        if (typeof cached === 'string') {
          if (!cancelled && cached) setResolvedUrl(cached);
          return;
        }

        const promise =
          cached && typeof (cached as any).then === 'function'
            ? (cached as Promise<string>)
            : api.lookupPoster(title, year, lookupLang);

        posterLookupCache.set(key, promise);
        const poster = await promise;
        if (cancelled) return;
        if (poster) {
          posterLookupCache.set(key, poster);
        } else {
          posterLookupCache.delete(key);
        }
        if (poster) setResolvedUrl(poster);
      } catch {
        // ignore
      }
    })();

    return () => {
      cancelled = true;
    };
  }, [isInView, language, posterUrl, resolvedUrl, title, year]);

  return (
    <div ref={ref} className={`lazy-poster ${className || ''}`}>
      {isInView ? (
        <img
          src={hasError ? fallback : (resolvedUrl || src)}
          alt={title}
          loading="lazy"
          decoding="async"
          onError={() => setHasError(true)}
        />
      ) : (
        <div className="lazy-poster-skeleton" aria-label={title} />
      )}
    </div>
  );
};
