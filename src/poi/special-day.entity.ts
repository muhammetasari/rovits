export interface SpecialDay {
    date: string;
    name: string;
    affectedCategories?: string[];
    isClosed: boolean;
    note?: string;
}

export const SPECIAL_DAYS: SpecialDay[] = [
    // ============ 2025 ============

    // YÄ±lbaÅŸÄ±
    {
        date: '2025-01-01',
        name: 'YÄ±lbaÅŸÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },

    // Ramazan BayramÄ± 2025 (30 Mart - 1 Nisan)
    {
        date: '2025-03-30',
        name: 'Ramazan BayramÄ± 1. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2025-03-31',
        name: 'Ramazan BayramÄ± 2. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2025-04-01',
        name: 'Ramazan BayramÄ± 3. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },

    // 23 Nisan
    {
        date: '2025-04-23',
        name: '23 Nisan Ulusal Egemenlik ve Ã‡ocuk BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - bazÄ± mÃ¼zeler kapalÄ± veya Ã¼cretsiz'
    },

    // 1 MayÄ±s
    {
        date: '2025-05-01',
        name: '1 MayÄ±s Emek ve DayanÄ±ÅŸma GÃ¼nÃ¼',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler kapalÄ±'
    },

    // 19 MayÄ±s
    {
        date: '2025-05-19',
        name: '19 MayÄ±s GenÃ§lik ve Spor BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler kapalÄ± olabilir'
    },

    // Kurban BayramÄ± 2025 (6-9 Haziran)
    {
        date: '2025-06-06',
        name: 'Kurban BayramÄ± 1. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2025-06-07',
        name: 'Kurban BayramÄ± 2. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2025-06-08',
        name: 'Kurban BayramÄ± 3. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2025-06-09',
        name: 'Kurban BayramÄ± 4. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },

    // 15 Temmuz
    {
        date: '2025-07-15',
        name: '15 Temmuz Demokrasi ve Milli Birlik GÃ¼nÃ¼',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler kapalÄ±'
    },

    // 30 AÄŸustos
    {
        date: '2025-08-30',
        name: '30 AÄŸustos Zafer BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler ve saraylar kapalÄ±'
    },

    // 29 Ekim
    {
        date: '2025-10-29',
        name: 'Cumhuriyet BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler ve saraylar kapalÄ±, Taksim ve meydanlar kalabalÄ±k'
    },

    // ============ 2026 ============

    // YÄ±lbaÅŸÄ±
    {
        date: '2026-01-01',
        name: 'YÄ±lbaÅŸÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },

    // Ramazan BayramÄ± 2026 (20-22 Mart)
    {
        date: '2026-03-20',
        name: 'Ramazan BayramÄ± 1. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2026-03-21',
        name: 'Ramazan BayramÄ± 2. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2026-03-22',
        name: 'Ramazan BayramÄ± 3. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },

    // 23 Nisan
    {
        date: '2026-04-23',
        name: '23 Nisan Ulusal Egemenlik ve Ã‡ocuk BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - bazÄ± mÃ¼zeler kapalÄ± veya Ã¼cretsiz'
    },

    // 1 MayÄ±s
    {
        date: '2026-05-01',
        name: '1 MayÄ±s Emek ve DayanÄ±ÅŸma GÃ¼nÃ¼',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler kapalÄ±'
    },

    // 19 MayÄ±s
    {
        date: '2026-05-19',
        name: '19 MayÄ±s GenÃ§lik ve Spor BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler kapalÄ± olabilir'
    },

    // Kurban BayramÄ± 2026 (27-30 MayÄ±s)
    {
        date: '2026-05-27',
        name: 'Kurban BayramÄ± 1. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2026-05-28',
        name: 'Kurban BayramÄ± 2. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2026-05-29',
        name: 'Kurban BayramÄ± 3. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },
    {
        date: '2026-05-30',
        name: 'Kurban BayramÄ± 4. GÃ¼n',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'MÃ¼zeler ve saraylar kapalÄ±'
    },

    // 15 Temmuz
    {
        date: '2026-07-15',
        name: '15 Temmuz Demokrasi ve Milli Birlik GÃ¼nÃ¼',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler kapalÄ±'
    },

    // 30 AÄŸustos
    {
        date: '2026-08-30',
        name: '30 AÄŸustos Zafer BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler ve saraylar kapalÄ±'
    },

    // 29 Ekim
    {
        date: '2026-10-29',
        name: 'Cumhuriyet BayramÄ±',
        affectedCategories: ['museum', 'palace'],
        isClosed: false,
        note: 'Resmi tatil - mÃ¼zeler ve saraylar kapalÄ±, Taksim ve meydanlar kalabalÄ±k'
    },
];