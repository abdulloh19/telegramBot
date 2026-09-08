import { NextRequest, NextResponse } from 'next/server';

// Memory cache for audio buffers to ensure instantaneous 0ms playback on repeat
const audioCache = new Map<string, { buffer: Buffer; contentType: string }>();

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const text = searchParams.get('text');
    const lang = searchParams.get('lang') || 'en';

    if (!text || !text.trim()) {
      return new NextResponse('Missing text parameter', { status: 400 });
    }

    const cleanText = text.trim();
    const cleanLang = lang.toLowerCase().startsWith('ru') ? 'ru' : 'en';
    const cacheKey = `${cleanLang}:${cleanText.toLowerCase()}`;

    // Check memory cache
    const cached = audioCache.get(cacheKey);
    if (cached) {
      return new NextResponse(new Uint8Array(cached.buffer), {
        headers: {
          'Content-Type': cached.contentType,
          'Cache-Control': 'public, max-age=31536000, immutable',
        },
      });
    }

    // Fetch from Google TTS with full desktop User-Agent to avoid 403 or empty responses
    const encodedText = encodeURIComponent(cleanText);
    const ttsUrl = `https://translate.google.com/translate_tts?ie=UTF-8&tl=${cleanLang}&client=tw-ob&q=${encodedText}`;

    const response = await fetch(ttsUrl, {
      headers: {
        'User-Agent':
          'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36',
        'Accept': '*/*',
        'Referer': 'https://translate.google.com/',
      },
    });

    if (!response.ok) {
      return new NextResponse(`TTS upstream error: ${response.status}`, { status: response.status });
    }

    const arrayBuffer = await response.arrayBuffer();
    const buffer = Buffer.from(arrayBuffer);

    // Save to memory cache
    audioCache.set(cacheKey, { buffer, contentType: 'audio/mpeg' });

    return new NextResponse(new Uint8Array(buffer), {
      headers: {
        'Content-Type': 'audio/mpeg',
        'Cache-Control': 'public, max-age=31536000, immutable',
        'Content-Length': buffer.length.toString(),
      },
    });
  } catch (error) {
    console.error('Error in /api/tts route:', error);
    return new NextResponse('Internal Server Error', { status: 500 });
  }
}
