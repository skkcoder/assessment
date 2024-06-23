package com.gift.go.assessment.security.repositories

import com.gift.go.assessment.security.domain.SecurityAuditInformation
import java.util.*
import org.springframework.data.repository.CrudRepository

interface SecurityAuditRepository : CrudRepository<SecurityAuditInformation, UUID>