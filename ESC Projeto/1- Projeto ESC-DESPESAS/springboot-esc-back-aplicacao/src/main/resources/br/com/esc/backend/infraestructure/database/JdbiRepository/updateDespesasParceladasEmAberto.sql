UPDATE
    tbd_DespesasParceladas
SET
    tp_Baixado = 'N'
WHERE
    tp_Baixado = 'S'
    AND id_Funcionario = :idFuncionario
    AND id_DespesaParcelada IN
        (SELECT DISTINCT(id_DespesaParcelada) FROM tbd_Parcelas WHERE tp_Baixado = 'N' AND id_Funcionario = :idFuncionario)