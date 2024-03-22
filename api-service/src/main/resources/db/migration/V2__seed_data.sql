-- Metric Definitions
INSERT INTO anonymized.metric_definitions (name, description, unit, is_active) VALUES
    ('Net Margin',           'Net income as a percentage of revenue',                    'PERCENTAGE', TRUE),
    ('Gross Margin',         'Gross profit as a percentage of revenue',                  'PERCENTAGE', TRUE),
    ('EBITDA Margin',        'EBITDA as a percentage of revenue',                        'PERCENTAGE', TRUE),
    ('Current Ratio',        'Current assets divided by current liabilities',            'INDEX',      TRUE),
    ('Quick Ratio',          'Liquid assets divided by current liabilities',             'INDEX',      TRUE),
    ('ROE',                  'Return on Equity',                                         'PERCENTAGE', TRUE),
    ('ROA',                  'Return on Assets',                                         'PERCENTAGE', TRUE),
    ('Debt-to-Equity',       'Total debt divided by shareholders equity',               'INDEX',      TRUE),
    ('Revenue Growth',       'Year-over-year revenue growth rate',                       'PERCENTAGE', TRUE),
    ('Operating Cash Flow',  'Cash generated from core operations',                      'CURRENCY',   TRUE),
    ('Days Sales Outstanding','Average collection period in days',                       'INDEX',      TRUE),
    ('Inventory Turnover',   'Cost of goods sold divided by average inventory',          'INDEX',      TRUE)
ON CONFLICT (name) DO NOTHING;

-- Companies
INSERT INTO anonymized.companies (name, sector_id, region, size) VALUES
    ('Alpha Manufacturing Co.',  'MANUFACTURING', 'SOUTH',     'MEDIUM'),
    ('Beta Retail Group',        'RETAIL',        'SOUTHEAST', 'LARGE'),
    ('Gamma Tech Solutions',     'TECHNOLOGY',    'MIDWEST',   'SMALL'),
    ('Delta Healthcare Ltd.',    'HEALTHCARE',    'NORTHEAST', 'MEDIUM')
ON CONFLICT DO NOTHING;

-- Demo Users (passwords: Admin@123, Analyst@123, Client@123 – BCrypt hashed)
INSERT INTO anonymized.users (email, password_hash, role, name, company_id) VALUES
    ('admin@dataanon.local',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN', 'Platform Admin', NULL),
    ('analyst@dataanon.local',
     '$2a$10$TrJSgjbR8FRXMmG7AonVrOePLVHqNX/D8lbUJcGY.GHpKJXpBVqNe',
     'ANALYST', 'Data Analyst', NULL),
    ('client.alpha@dataanon.local',
     '$2a$10$TrJSgjbR8FRXMmG7AonVrOePLVHqNX/D8lbUJcGY.GHpKJXpBVqNe',
     'CLIENT', 'Alpha Client User',
     (SELECT id FROM anonymized.companies WHERE name = 'Alpha Manufacturing Co.' LIMIT 1)),
    ('client.beta@dataanon.local',
     '$2a$10$TrJSgjbR8FRXMmG7AonVrOePLVHqNX/D8lbUJcGY.GHpKJXpBVqNe',
     'CLIENT', 'Beta Client User',
     (SELECT id FROM anonymized.companies WHERE name = 'Beta Retail Group' LIMIT 1))
ON CONFLICT (email) DO NOTHING;
