INSERT INTO permissions (name) VALUES
                                   ('CREATE'),
                                   ('READ'),
                                   ('DELETE'),
                                   ('SUSCRIBE')
    ON CONFLICT (name) DO NOTHING;
