-- ==============================================================================
-- NumiTerra PostgreSQL + PostGIS Initialization Script
-- Description: Core schema for Distributed AI, IoT, and Spatial-Commerce
-- ==============================================================================

-- 1. Enable Required Extensions
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ==========================================
-- CORE USERS & IDENTITY
-- ==========================================
CREATE TABLE farmer (
                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        dob DATE,
                        nationality VARCHAR(100),
                        phone_number VARCHAR(50) UNIQUE,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        country VARCHAR(100),
                        city VARCHAR(100),
                        district VARCHAR(100),
                        postal_code VARCHAR(20),
                        national_id VARCHAR(100) UNIQUE,
                        specialty VARCHAR(255),
                        password_hash VARCHAR(255) NOT NULL,
                        role VARCHAR(50) CHECK (role IN ('FARMER', 'ADMIN')) NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- AGRONOMY & SUSTAINABILITY REFERENCE DATA
-- ==========================================
CREATE TABLE crop (
                      id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                      name VARCHAR(255) NOT NULL,
                      scientific_name VARCHAR(255),
                      variety VARCHAR(255),
                      crop_family VARCHAR(100),
                      crop_type VARCHAR(100),

                      growth_cycle_days INT,
                      germination_days INT,
                      maturity_days INT,

                      optimal_temperature_min DOUBLE PRECISION,
                      optimal_temperature_max DOUBLE PRECISION,
                      rainfall_requirement_mm DOUBLE PRECISION,
                      drought_tolerance_index DOUBLE PRECISION,

                      optimal_soil_ph_min DOUBLE PRECISION,
                      optimal_soil_ph_max DOUBLE PRECISION,
                      preferred_soil_texture VARCHAR(100),

                      water_requirement_mm_per_season DOUBLE PRECISION,

                      nitrogen_requirement_kg_per_ha DOUBLE PRECISION,
                      phosphorus_requirement_kg_per_ha DOUBLE PRECISION,
                      potassium_requirement_kg_per_ha DOUBLE PRECISION,

                      recommended_rotation_gap_years INT,
                      nitrogen_fixing BOOLEAN DEFAULT FALSE,

                      average_yield_ton_per_ha DOUBLE PRECISION,
                      max_yield_ton_per_ha DOUBLE PRECISION,

                      common_diseases TEXT,
                      common_pests TEXT,

                      created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE crop_rotation_rule (
                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                    previous_crop_id UUID REFERENCES crop(id) ON DELETE CASCADE,
                                    next_crop_id UUID REFERENCES crop(id) ON DELETE CASCADE,
                                    suitability_score DOUBLE PRECISION CHECK (suitability_score >= 0 AND suitability_score <= 10),
                                    recommended_wait_years INT,
                                    agronomic_reason TEXT,
                                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE soil_profile (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              soil_type_name VARCHAR(100) NOT NULL,
                              ph_range VARCHAR(50),
                              organic_matter_percentage DOUBLE PRECISION,
                              texture_classification VARCHAR(100)
);

CREATE TABLE disease (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         name VARCHAR(255) NOT NULL,
                         scientific_name VARCHAR(255),
                         symptoms_description TEXT,
                         treatment_recommendation TEXT,
                         severity_level VARCHAR(50) CHECK (severity_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
                         ai_generated_flag BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- AGGREGATE ROOT: FARM ZONE
-- ==========================================
CREATE TABLE farm_zone (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           farmer_id UUID REFERENCES farmer(id) ON DELETE CASCADE NOT NULL,
                           current_crop_id UUID REFERENCES crop(id) ON DELETE SET NULL,
                           soil_profile_id UUID REFERENCES soil_profile(id) ON DELETE SET NULL,
                           name VARCHAR(255) NOT NULL,
                           description TEXT,

    -- PostGIS Spatial Data Columns
                           boundary geometry(MULTIPOLYGON, 4326),
                           centroid geometry(POINT, 4326),

                           area_hectares DOUBLE PRECISION,
                           elevation_avg DOUBLE PRECISION,

                           status VARCHAR(50) CHECK (status IN ('ACTIVE', 'FALLOW', 'ARCHIVED')) NOT NULL,
                           planting_date DATE,
                           expected_harvest_date DATE,
                           irrigation_type VARCHAR(50) CHECK (irrigation_type IN ('DRIP', 'SPRINKLER', 'FLOOD')),
                           cultivation_method VARCHAR(50) CHECK (cultivation_method IN ('ORGANIC', 'CONVENTIONAL')),
                           crop_rotation_cycle_years INT,

                           marketplace_visibility_radius_km DOUBLE PRECISION DEFAULT 25.0,
                           allow_external_offers BOOLEAN DEFAULT TRUE,

                           created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Spatial Indexes for fast bounding box / radius queries
CREATE INDEX idx_farm_zone_boundary ON farm_zone USING GIST (boundary);
CREATE INDEX idx_farm_zone_centroid ON farm_zone USING GIST (centroid);

CREATE TABLE farm_zone_boundary_version (
                                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                            farm_zone_id UUID REFERENCES farm_zone(id) ON DELETE CASCADE,
                                            boundary geometry(MULTIPOLYGON, 4326) NOT NULL,
                                            valid_from TIMESTAMP WITH TIME ZONE NOT NULL,
                                            valid_to TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_fz_boundary_version_geom ON farm_zone_boundary_version USING GIST (boundary);

-- ==========================================
-- ZONE ENTITIES & HISTORICAL DATA
-- ==========================================
CREATE TABLE farm_zone_crop_history (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        farm_zone_id UUID REFERENCES farm_zone(id) ON DELETE CASCADE NOT NULL,
                                        crop_id UUID REFERENCES crop(id) ON DELETE RESTRICT NOT NULL,

                                        season_year INT NOT NULL,
                                        season_type VARCHAR(100),
                                        planting_date DATE,
                                        harvest_date DATE,

                                        cultivated_area_hectares DOUBLE PRECISION,
                                        yield_tons DOUBLE PRECISION,
                                        yield_ton_per_hectare DOUBLE PRECISION,

                                        fertilizer_used_kg DOUBLE PRECISION,
                                        nitrogen_applied_kg DOUBLE PRECISION,
                                        phosphorus_applied_kg DOUBLE PRECISION,
                                        potassium_applied_kg DOUBLE PRECISION,

                                        irrigation_water_liters DOUBLE PRECISION,
                                        irrigation_events INT,

                                        pesticide_applications INT,
                                        disease_incidents_count INT,
                                        major_disease_detected VARCHAR(255),

                                        rainfall_mm DOUBLE PRECISION,
                                        average_temperature DOUBLE PRECISION,

                                        production_cost DOUBLE PRECISION,
                                        revenue DOUBLE PRECISION,
                                        profit DOUBLE PRECISION,

                                        carbon_emission_estimate DOUBLE PRECISION,
                                        water_efficiency_index DOUBLE PRECISION,

                                        farmer_notes TEXT,
                                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE farm_zone_sustainability_report (
                                                 id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                                 farm_zone_id UUID REFERENCES farm_zone(id) ON DELETE CASCADE NOT NULL,
                                                 reporting_period_start DATE NOT NULL,
                                                 reporting_period_end DATE NOT NULL,

                                                 carbon_emission_kg DOUBLE PRECISION,
                                                 water_consumption_liters DOUBLE PRECISION,
                                                 nitrogen_balance DOUBLE PRECISION,
                                                 biodiversity_score DOUBLE PRECISION,
                                                 eu_compliance_score DOUBLE PRECISION,

                                                 created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE diagnostic_report (
                                   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                   zone_id UUID REFERENCES farm_zone(id) ON DELETE CASCADE,
                                   user_id UUID REFERENCES farmer(id) ON DELETE CASCADE NOT NULL,
                                   detected_disease_id UUID REFERENCES disease(id) ON DELETE SET NULL,
                                   suggested_crop_id UUID REFERENCES crop(id) ON DELETE SET NULL,

                                   image_url VARCHAR(1024),
                                   ai_confidence DOUBLE PRECISION,
                                   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE task (
                      id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                      zone_id UUID REFERENCES farm_zone(id) ON DELETE CASCADE,
                      assigned_to UUID REFERENCES farmer(id) ON DELETE SET NULL,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      status VARCHAR(50) CHECK (status IN ('PENDING', 'IN_PROGRESS', 'DONE')) DEFAULT 'PENDING',
                      due_date DATE,
                      created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- IOT, TELEMETRY & ACTUATORS
-- ==========================================
CREATE TABLE iot_device (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            farm_zone_id UUID REFERENCES farm_zone(id) ON DELETE CASCADE NOT NULL,
                            device_category VARCHAR(50) CHECK (device_category IN ('SENSOR', 'ACTUATOR')) NOT NULL,
                            device_type VARCHAR(50) CHECK (device_type IN ('MOISTURE', 'TEMP', 'PH', 'NPK', 'HUMIDITY', 'WATER_PUMP', 'VALVE')) NOT NULL,
                            mac_address VARCHAR(17) UNIQUE NOT NULL,
                            installation_date DATE,
                            status VARCHAR(50) CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE', 'ERROR')) DEFAULT 'ACTIVE',
                            location geometry(POINT, 4326)
);
CREATE INDEX idx_iot_device_location ON iot_device USING GIST (location);

-- BIGSERIAL used for high-frequency inserts
CREATE TABLE telemetry (
                           id BIGSERIAL PRIMARY KEY,
                           device_id UUID REFERENCES iot_device(id) ON DELETE CASCADE NOT NULL,
                           reading_value DOUBLE PRECISION NOT NULL,
                           recorded_at TIMESTAMP WITH TIME ZONE NOT NULL
);
-- B-Tree Index for fast time-series retrieval and Kafka consumer lookups
CREATE INDEX idx_telemetry_recorded_at ON telemetry (recorded_at DESC);
CREATE INDEX idx_telemetry_device_id ON telemetry (device_id);

CREATE TABLE device_action_log (
                                   id BIGSERIAL PRIMARY KEY,
                                   device_id UUID REFERENCES iot_device(id) ON DELETE CASCADE NOT NULL,
                                   action_type VARCHAR(50) CHECK (action_type IN ('TURN_ON', 'TURN_OFF', 'SET_VALUE')) NOT NULL,
                                   action_value DOUBLE PRECISION,
                                   triggered_by VARCHAR(50) CHECK (triggered_by IN ('MANUAL_USER', 'AI_AUTO', 'SCHEDULE')) NOT NULL,
                                   executed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_device_action_log_time ON device_action_log (executed_at DESC);

-- ==========================================
-- GEO-SPATIAL MARKETPLACE
-- ==========================================
CREATE TABLE marketplace_listing (
                                     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                     seller_id UUID REFERENCES farmer(id) ON DELETE CASCADE NOT NULL,
                                     title VARCHAR(255) NOT NULL,
                                     description TEXT,
                                     price DECIMAL(12,2) NOT NULL,
                                     location geometry(POINT, 4326) NOT NULL,
                                     status VARCHAR(50) CHECK (status IN ('ACTIVE', 'SOLD', 'CANCELLED')) DEFAULT 'ACTIVE',
                                     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- Crucial for Module C (Spatial Bounding Box querying for localized items)
CREATE INDEX idx_marketplace_location ON marketplace_listing USING GIST (location);

CREATE TABLE bid_message (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             listing_id UUID REFERENCES marketplace_listing(id) ON DELETE CASCADE NOT NULL,
                             bidder_id UUID REFERENCES farmer(id) ON DELETE CASCADE NOT NULL,
                             amount DECIMAL(12,2) NOT NULL,
                             message TEXT,
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);